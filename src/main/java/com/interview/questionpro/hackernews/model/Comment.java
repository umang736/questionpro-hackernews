package com.interview.questionpro.hackernews.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "comment")
public class Comment {
	
	@Id
	private Long id;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;
    
    @Column(name="child_comments_count")
    private Integer childCommentsCount;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment", cascade={CascadeType.ALL})
    private List<Comment> childComments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_story_id")
	private Story parentStory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_comment_id")
	private Comment parentComment;

    @Column
    private String text;

    @Column(name="create_time")
    private LocalDateTime createTime;
    
    @Column
    private boolean deleted;
    
    @Column(name="data_fetched_at")
    private LocalDateTime dataFetchedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Integer getChildCommentsCount() {
		return childCommentsCount;
	}

	public void setChildCommentsCount(Integer childCommentsCount) {
		this.childCommentsCount = childCommentsCount;
	}

	public List<Comment> getChildComments() {
		return childComments;
	}

	public void setChildComments(List<Comment> childComments) {
		this.childComments = childComments;
	}

	public Story getParentStory() {
		return parentStory;
	}

	public void setParentStory(Story parentStory) {
		this.parentStory = parentStory;
	}

	public Comment getParentComment() {
		return parentComment;
	}

	public void setParentComment(Comment parentComment) {
		this.parentComment = parentComment;
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

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public LocalDateTime getDataFetchedAt() {
		return dataFetchedAt;
	}

	public void setDataFetchedAt(LocalDateTime dataFetchedAt) {
		this.dataFetchedAt = dataFetchedAt;
	}
    
}
