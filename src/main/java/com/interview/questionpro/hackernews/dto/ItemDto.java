package com.interview.questionpro.hackernews.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import com.interview.questionpro.hackernews.model.Comment;
import com.interview.questionpro.hackernews.model.Story;
import com.interview.questionpro.hackernews.model.User;

public class ItemDto {

	private Long id;
	private String type;
    private String by;
    private Long time;
    private String text;
    private Long parent;
    private List<Long> kids;
    private String url;
    private Long score;
    private String title;
    

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBy() {
		return by;
	}
	public void setBy(String by) {
		this.by = by;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getParent() {
		return parent;
	}
	public void setParent(Long parent) {
		this.parent = parent;
	}
	public List<Long> getKids() {
		return kids;
	}
	public void setKids(List<Long> kids) {
		this.kids = kids;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getScore() {
		return score;
	}
	public void setScore(Long score) {
		this.score = score;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "ItemDto [id=" + id + ", type=" + type + ", by=" + by + ", time=" + time + ", text=" + text + ", parent="
				+ parent + ", kids=" + kids + ", url=" + url + ", score=" + score + ", title=" + title + "]";
	}
	
}
