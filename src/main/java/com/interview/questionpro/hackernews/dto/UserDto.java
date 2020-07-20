package com.interview.questionpro.hackernews.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import com.interview.questionpro.hackernews.model.User;

public class UserDto {

	private String id;
	private Long created;
	private String about;
//	private List<Long> submitted;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getCreated() {
		return created;
	}
	public void setCreated(Long created) {
		this.created = created;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
//	public List<Long> getSubmitted() {
//		return submitted;
//	}
//	public void setSubmitted(List<Long> submitted) {
//		this.submitted = submitted;
//	}
	
	@Override
	public String toString() {
//		return "UserDto [id=" + id + ", created=" + created + ", about=" + about + ", submitted=" + submitted + "]";
		return "UserDto [id=" + id + ", created=" + created + ", about=" + about + "]";
	}
	
	public User toEntity() {
		User user = new User();
		user.setId(id);
		user.setCreateTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(created), ZoneId.systemDefault()));
		return user;
	}
}
