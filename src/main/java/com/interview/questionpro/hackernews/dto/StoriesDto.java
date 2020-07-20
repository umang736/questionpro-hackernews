package com.interview.questionpro.hackernews.dto;

import java.util.List;

public class StoriesDto {
	
	List<StoryDto> stories;

	public List<StoryDto> getStories() {
		return stories;
	}

	public void setStories(List<StoryDto> stories) {
		this.stories = stories;
	}

	@Override
	public String toString() {
		return "StoriesDto [stories=" + stories + "]";
	}
	
}
