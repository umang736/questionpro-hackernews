package com.interview.questionpro.hackernews.dto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.interview.questionpro.hackernews.model.Comment;
import com.interview.questionpro.hackernews.model.User;

public class CommentDto {

	private Long id;
	private CommentAuthorDto author;
	private Integer childCommentsCount;
	private String text;
    private LocalDateTime createTime;
    
    public CommentDto() {
    	
    }

    public CommentDto(Comment comment) {
    	this.setId(comment.getId());
    	this.setAuthor(new CommentAuthorDto(comment.getAuthor()));
    	this.setChildCommentsCount(comment.getChildCommentsCount());
    	this.setText(comment.getText());
    	this.setCreateTime(comment.getCreateTime());
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CommentAuthorDto getAuthor() {
		return author;
	}

	public void setAuthor(CommentAuthorDto author) {
		this.author = author;
	}

	public Integer getChildCommentsCount() {
		return childCommentsCount;
	}

	public void setChildCommentsCount(Integer childCommentsCount) {
		this.childCommentsCount = childCommentsCount;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		return "CommentDto [id=" + id + ", author=" + author + ", childCommentsCount=" + childCommentsCount + ", text=" + text + ", createTime=" + createTime + "]";
	}

	public static class CommentAuthorDto {

		private String id;
        private long profileAgeInYears;

        public CommentAuthorDto() {
        	
        }
        public CommentAuthorDto(User user) {
        	if (user != null) {
	        	this.setId(user.getId());
	        	this.setProfileAgeInYears(ChronoUnit.YEARS.between(user.getCreateTime(), LocalDateTime.now()));
        	}
        }
        public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public long getProfileAgeInYears() {
			return profileAgeInYears;
		}
		public void setProfileAgeInYears(long profileAgeInYears) {
			this.profileAgeInYears = profileAgeInYears;
		}

		@Override
		public String toString() {
			return "CommentAuthorDto [id=" + id + ", profileAgeInYears=" + profileAgeInYears + "]";
		}
        
    }
	    
}
