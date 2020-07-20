package com.interview.questionpro.hackernews.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.interview.questionpro.hackernews.comparators.BestStoriesComparator;
import com.interview.questionpro.hackernews.comparators.BestStoryCommentsComparator;
import com.interview.questionpro.hackernews.constant.Constants;
import com.interview.questionpro.hackernews.dto.CommentDto;
import com.interview.questionpro.hackernews.dto.CommentsDto;
import com.interview.questionpro.hackernews.dto.ItemDto;
import com.interview.questionpro.hackernews.dto.StoriesDto;
import com.interview.questionpro.hackernews.dto.StoryDto;
import com.interview.questionpro.hackernews.dto.UserDto;
import com.interview.questionpro.hackernews.exception.ApplicationException;
import com.interview.questionpro.hackernews.model.Comment;
import com.interview.questionpro.hackernews.model.Story;
import com.interview.questionpro.hackernews.model.User;
import com.interview.questionpro.hackernews.repository.CommentRepository;
import com.interview.questionpro.hackernews.repository.StoryRepository;
import com.interview.questionpro.hackernews.repository.UserRepository;
import com.interview.questionpro.hackernews.service.HackerNewsService;
import com.interview.questionpro.hackernews.task.FetchItemTask;
import com.interview.questionpro.hackernews.task.FetchUserTask;

@Service
public class HackerNewsServiceImpl implements HackerNewsService {

	@Autowired
	private StoryRepository storyRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private UserRepository userRepository;
	
	private final RestTemplate restTemplate;
	private final BestStoryCommentsComparator bestStoryCommentsComparator;
	private final BestStoriesComparator bestStoriesComparator;
	
	private final Pageable bestStoriesListPageable = PageRequest.of(0, Constants.BEST_STORIES_LIST_COUNT, Sort.by(Sort.Direction.DESC, "score"));
	private final Pageable commentsListPageable = PageRequest.of(0, Constants.COMMENTS_LIST_COUNT, Sort.by(Sort.Direction.DESC, "childCommentsCount"));
	private final static Logger logger = LoggerFactory.getLogger(HackerNewsServiceImpl.class);

	public HackerNewsServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.bestStoriesComparator = new BestStoriesComparator();
        this.bestStoryCommentsComparator = new BestStoryCommentsComparator();
    }
	
	@Override
	public StoriesDto getBestStoriesSortedByScore() {
		logger.info("getBestStoriesSortedByScore called");
		StoriesDto storiesDto = new StoriesDto();
		LocalDateTime dataFetchedAfter = LocalDateTime.now().minusSeconds(Constants.HACKERNEWS_CACHED_DATA_VALID_FOR_SECONDS);
		List<Story> stories = storyRepository.findByDataFetchedAtGreaterThan(dataFetchedAfter, bestStoriesListPageable);
		List<StoryDto> storyDtos = stories.stream().map(s -> new StoryDto(s)).collect(Collectors.toList());
		storiesDto.setStories(storyDtos);
		return storiesDto;
	}

	@Override
	@Scheduled(fixedDelayString  = "${hackernews.cached-data.valid-for.seconds}000", initialDelay = 0)
	public List<Story> fetchAndSaveBestStories() {
		logger.info("fetchAndSaveBestStories called");
		Long[] bestStoryIds = this.getBestStories();
		List<Callable<Pair<Long, ItemDto>>> tasks = new ArrayList<>();
	    for(Long bestStoryId : bestStoryIds){
	      tasks.add(new FetchItemTask(bestStoryId, restTemplate));
	    }
	    ExecutorService executor = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);
		List<Future<Pair<Long, ItemDto>>> storyFutures = new ArrayList<>();
		for (Callable<Pair<Long, ItemDto>> task : tasks) {
	    	Future<Pair<Long, ItemDto>> pendingResult= executor.submit(task);
	    	storyFutures.add(pendingResult);
	    }
		// This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {
        }
		List<ItemDto> storyItems= new ArrayList<ItemDto>();
        for (Future<Pair<Long, ItemDto>> pendingResult : storyFutures) {
            try {
            	Pair<Long, ItemDto> pair = pendingResult.get();
            	logger.info("FetchItemTask completed for story id {}, response = {}", pair.getLeft(), pair.getRight());
            	if(pair.getRight() != null) {
            		storyItems.add(pair.getRight());
            	}
            } catch (InterruptedException | ExecutionException e) {
                logger.error("FetchItemTask for story got exception", e);
            }
        }

        Collections.sort(storyItems, this.bestStoriesComparator);
        int selectTopBestStoryCount = Math.min(Constants.BEST_STORIES_LIST_COUNT, storyItems.size());
		List<ItemDto> topBestStoryItems = storyItems.subList(0, selectTopBestStoryCount);

		List<Story> topBestStories = new ArrayList<>();
		for(ItemDto storyItem: topBestStoryItems) {
			if (storyItem.getKids() == null) {
				storyItem.setKids(Collections.emptyList());
			}
			List<ItemDto> commentItems = this.fetchStoryComments(storyItem);
			topBestStories.add(this.saveBestStoryItem(storyItem, commentItems));
		}
		return topBestStories;
	}
	
	public List<ItemDto> fetchStoryComments(ItemDto storyItem) {
		List<Long> commentIds = storyItem.getKids();
		List<Callable<Pair<Long, ItemDto>>> tasks = new ArrayList<>();
	    for(Long commentId : commentIds) {
	      tasks.add(new FetchItemTask(commentId, restTemplate));
	    }
	    ExecutorService executor = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);
		List<Future<Pair<Long, ItemDto>>> commentFutures = new ArrayList<>();
		for (Callable<Pair<Long, ItemDto>> task : tasks) {
	    	Future<Pair<Long, ItemDto>> pendingResult= executor.submit(task);
	    	commentFutures.add(pendingResult);
	    }
		// This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {
        }
		List<ItemDto> commentItems= new ArrayList<ItemDto>();
        for (Future<Pair<Long, ItemDto>> pendingResult : commentFutures) {
            try {
            	Pair<Long, ItemDto> pair = pendingResult.get();
            	logger.info("FetchItemTask completed for comment id {}, response = {}", pair.getLeft(), pair.getRight());
            	if(pair.getRight() != null) {
            		commentItems.add(pair.getRight());
            	}
            } catch (InterruptedException | ExecutionException e) {
                logger.error("FetchItemTask for comment got exception", e);
            }
        }
        commentItems.forEach(commentItem -> {
			if (commentItem.getKids() == null) {
				commentItem.setKids(Collections.emptyList());
			}
        });

        Collections.sort(commentItems, this.bestStoryCommentsComparator);
        int selectTopCommentsCount = Math.min(Constants.COMMENTS_LIST_COUNT, commentItems.size());
		List<ItemDto> topBestStoryCommentItems = commentItems.subList(0, selectTopCommentsCount);
        return topBestStoryCommentItems;
	}

	private Story saveBestStoryItem(ItemDto storyItem, List<ItemDto> commentItems) {
		LocalDateTime storyDataLastFetchedAt = LocalDateTime.now();

		Set<String> userNames = new HashSet<String>();
        userNames.add(storyItem.getBy());
        commentItems.forEach( c -> userNames.add(c.getBy()));
        List<User> users = this.fetchAndSaveUsers(userNames);

        return this.saveStoryAndItsComments(storyItem, commentItems, users, storyDataLastFetchedAt);
	}
	
	@Override
	public StoriesDto getPastBestStories() {
		logger.info("getPastBestStories called");
		StoriesDto storiesDto = new StoriesDto();
		List<Story> bestStories = storyRepository.findAll();
		List<StoryDto> storyDtoList = bestStories.stream().map(s -> new StoryDto(s)).collect(Collectors.toList());
		storiesDto.setStories(storyDtoList);
		return storiesDto;
	}
	
	@Transactional
	private Story saveStoryAndItsComments(ItemDto storyItem, List<ItemDto> commentItems, List<User> users, LocalDateTime dataFetchedAt) {	
        
        Map<String, User> idToUserMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity())); 
		Map<Long, ItemDto> commentIdToItemDtoMap = commentItems.stream().collect(Collectors.toMap(ItemDto::getId, Function.identity()));

		Optional<Story> storyOpt = storyRepository.findWithCommentsById(storyItem.getId());
		
		Map<ItemDto, Comment> itemDtoToCommentMap = new HashMap<>();
		Story story = null;
		List<Comment> storyComments = null;
		if (storyOpt.isPresent()) {
			story = storyOpt.get();
			storyComments = story.getComments();
			Map<Long, Comment> itemIdToCommentMap = new HashMap<>();
			storyComments.forEach(c -> {
				if (!commentIdToItemDtoMap.containsKey(c.getId())) {
					c.setDeleted(true);
				}
			});
			storyComments.forEach(c -> {
				if (commentIdToItemDtoMap.containsKey(c.getId())) {
					itemIdToCommentMap.put(c.getId(), c);
				}
			});
			for(ItemDto commentItem : commentItems) {
				if (itemIdToCommentMap.containsKey(commentItem.getId())) {
					itemDtoToCommentMap.put(commentItem, itemIdToCommentMap.get(commentItem.getId()));
				} else {
					Comment comment = new Comment();
					comment.setParentStory(story);
					itemDtoToCommentMap.put(commentItem, comment);
					storyComments.add(comment);
				}
			};			
			
		} else {
			story = new Story();
			storyComments = new ArrayList<>();
			for(ItemDto commentItem : commentItems){
				Comment comment = new Comment();
				comment.setParentStory(story);
				itemDtoToCommentMap.put(commentItem, comment);
				storyComments.add(comment);
			};
			story.setComments(storyComments);
		}
		this.updateStoryDetails(storyItem, story);
		story.setDataFetchedAt(dataFetchedAt);
		for(Map.Entry<ItemDto, Comment> entry : itemDtoToCommentMap.entrySet()) {
			ItemDto commentItem = entry.getKey();
			Comment comment = entry.getValue();
			User author = idToUserMap.get(commentItem.getBy());
			this.updateCommentDetails(comment, commentItem, author);
			comment.setDataFetchedAt(dataFetchedAt);
		}
		return storyRepository.saveAndFlush(story);
	}

	@Override
	public CommentsDto getCommentsSortedByChildComments(Long storyId) {
		Optional<Story> storyOpt = storyRepository.findById(storyId);
		if (!storyOpt.isPresent()) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST, "Story not found", "storyId invalid");
		}
		Story story = storyOpt.get();
		CommentsDto commentsDto = new CommentsDto();
		List<Comment> storyComments = commentRepository.findByParentStoryAndDeleted(story, false, commentsListPageable);
		List<CommentDto> commentDtoList = storyComments.stream().map(c -> new CommentDto(c)).collect(Collectors.toList());
		commentsDto.setComments(commentDtoList);
		return commentsDto;
	}

	@Override
	public List<User> fetchAndSaveUsers(Set<String> userNamesSet) {
		List<String> userNamesList = userNamesSet.stream().collect(Collectors.toList());
        List<User> usersInDbList = userRepository.findByIdIn(userNamesList);
        Set<String> userNamesInDbSet = usersInDbList.stream().map(User::getId).collect(Collectors.toSet());
        List<String> userNamesNotInDb = userNamesList.stream().filter( u -> !userNamesInDbSet.contains(u)).collect(Collectors.toList());
        
		List<Callable<Pair<String, UserDto>>> tasks = new ArrayList<>();
	    for(String userName : userNamesNotInDb) {
	      tasks.add(new FetchUserTask(userName, restTemplate));
	    }
	    ExecutorService executor = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);
		List<Future<Pair<String, UserDto>>> userFutures = new ArrayList<>();
		for (Callable<Pair<String, UserDto>> task : tasks) {
	    	Future<Pair<String, UserDto>> pendingResult= executor.submit(task);
	    	userFutures.add(pendingResult);
	    }
		// This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {
        }
		List<UserDto> userDtos= new ArrayList<UserDto>();
        for (Future<Pair<String, UserDto>> pendingResult : userFutures) {
            try {
            	Pair<String, UserDto> pair = pendingResult.get();
            	logger.info("FetchUserTask completed for user id {}, response = {}", pair.getLeft(), pair.getRight());
            	if(pair.getRight() != null) {
            		userDtos.add(pair.getRight());
            	}
            } catch (InterruptedException | ExecutionException e) {
                logger.error("FetchUserTask for user got exception", e);
            }
        }
		
        LocalDateTime dataLastFetchedAt = LocalDateTime.now();
        List<User> newUsers = this.saveUsers(userDtos, dataLastFetchedAt);
        List<User> users = new ArrayList<>(userNamesSet.size());
        users.addAll(usersInDbList); users.addAll(newUsers);
        return users;
	}

	private List<User> saveUsers(List<UserDto> userDtos, LocalDateTime dataFetchedAt) {
		logger.info("saveUsers called for {}", userDtos);
		if (userDtos.size() > 0) {
			List<User> newUsers= userDtos.stream().map( userDto -> {
				User user = userDto.toEntity();
				user.setDataFetchedAt(dataFetchedAt);
				return user;
			}).collect(Collectors.toList());
			return userRepository.saveAll(newUsers);
		} else {
			return Collections.emptyList();
		}
	}
	
	private String getHackerNewsApiEndPointUrl (String apiPath) {
		return new StringBuilder(Constants.HACKER_NEWS_APIS_PREFIX).append(apiPath).append(Constants.HACKER_NEWS_APIS_SUFFIX).toString();
	}
	
	private Long[] getBestStories() {
		String endPointUrl = getHackerNewsApiEndPointUrl(Constants.HACKER_NEWS_BEST_STORIES_API_PATH);
		try {
			ResponseEntity<Long[]> response = this.restTemplate.getForEntity(endPointUrl, Long[].class);
			return response.getBody();
		} catch (RestClientException ex) {
			logger.error("getBestStories failed", ex);
			return null;
		}
	}
	
	private void updateCommentDetails(Comment comment, ItemDto itemDto, User author) {
		comment.setId(itemDto.getId());
		comment.setAuthor(author);
		comment.setChildCommentsCount(itemDto.getKids().size());
		comment.setDeleted(false);
		comment.setText(itemDto.getText());
		comment.setCreateTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(itemDto.getTime()), ZoneId.systemDefault()));
	}
		
	private void updateStoryDetails(ItemDto itemDto, Story story) {
		story.setId(itemDto.getId());
		story.setAuthorId(itemDto.getBy());
		story.setScore(itemDto.getScore());
		story.setCreateTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(itemDto.getTime()), ZoneId.systemDefault()));
		story.setTitle(itemDto.getTitle());
		story.setUrl(itemDto.getUrl());
	}
//	
//	private boolean isRecentlyUpdated(LocalDateTime dataFetchedAt) {
//		return ChronoUnit.SECONDS.between(dataFetchedAt, LocalDateTime.now()) < Constants.HACKERNEWS_CACHED_DATA_VALID_FOR_SECONDS;
//	}

}
