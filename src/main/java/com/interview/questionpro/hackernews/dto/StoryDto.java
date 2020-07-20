package com.interview.questionpro.hackernews.dto;

import java.time.LocalDateTime;

import com.interview.questionpro.hackernews.model.Story;

public class StoryDto {

	private Long id;
    private String authorId;
    private Long score;
    private LocalDateTime createTime;
    private String title;
    private String url;

    public StoryDto() {
    	
    }

    public StoryDto(Story story) {
    	this.setId(story.getId());
    	this.setAuthorId(story.getAuthorId());
    	this.setScore(story.getScore());
    	this.setCreateTime(story.getCreateTime());
    	this.setTitle(story.getTitle());
    	this.setUrl(story.getUrl());
    }
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public Long getScore() {
		return score;
	}
	public void setScore(Long score) {
		this.score = score;
	}
	public LocalDateTime getCreateTime() {
		return createTime;
	}
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return "StoryDto [id=" + id + ", authorId=" + authorId + ", score=" + score + ", createTime="
				+ createTime + ", title=" + title + ", url=" + url + "]";
	}
    
}
