package com.interview.questionpro.hackernews.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interview.questionpro.hackernews.dto.CommentsDto;
import com.interview.questionpro.hackernews.dto.StoriesDto;
import com.interview.questionpro.hackernews.service.HackerNewsService;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("hackernews")
public class HackerNewsController {

	@Autowired
	private HackerNewsService hackerNewsService;

	private final static Logger logger = LoggerFactory.getLogger(HackerNewsController.class);
	
	@GetMapping("/best-stories")
	public ResponseEntity<StoriesDto> getBestStoriesSortedByScore() {
		StoriesDto storiesDto = hackerNewsService.getBestStoriesSortedByScore();
		return new ResponseEntity<StoriesDto>(storiesDto, HttpStatus.OK);
	}
	
	@GetMapping("/past-stories")
	public ResponseEntity<StoriesDto> getPastBestStories() {
		StoriesDto storiesDto = hackerNewsService.getPastBestStories();
		return new ResponseEntity<StoriesDto>(storiesDto, HttpStatus.OK);
	}
	
	@GetMapping("/story/{storyId}/comments")
	public ResponseEntity<CommentsDto> getCommentsSortedByChildComments(@PathVariable("storyId") Long storyId) {
		CommentsDto commentsDto = hackerNewsService.getCommentsSortedByChildComments(storyId);
		return new ResponseEntity<CommentsDto>(commentsDto, HttpStatus.OK);
	}
}
