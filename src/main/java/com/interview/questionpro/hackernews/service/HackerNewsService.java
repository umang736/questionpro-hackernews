package com.interview.questionpro.hackernews.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.interview.questionpro.hackernews.dto.CommentsDto;
import com.interview.questionpro.hackernews.dto.StoriesDto;
import com.interview.questionpro.hackernews.model.Comment;
import com.interview.questionpro.hackernews.model.Story;
import com.interview.questionpro.hackernews.model.User;

public interface HackerNewsService {
	
	StoriesDto getBestStoriesSortedByScore();
	List<Story> fetchAndSaveBestStories();
	StoriesDto getPastBestStories();
	CommentsDto getCommentsSortedByChildComments(Long storyId);
	List<User> fetchAndSaveUsers(Set<String> userNamesSet);
}
