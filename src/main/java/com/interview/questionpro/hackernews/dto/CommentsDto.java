package com.interview.questionpro.hackernews.dto;

import java.util.List;

public class CommentsDto {

	List<CommentDto> comments;

	public List<CommentDto> getComments() {
		return comments;
	}

	public void setComments(List<CommentDto> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "CommentsDto [comments=" + comments + "]";
	}
	
}
