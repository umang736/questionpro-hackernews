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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "story")
public class Story {
	
	@Id
	private Long id;
    
    @Column(name="author_id")
    private String authorId;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentStory", cascade={CascadeType.ALL})
    private List<Comment> comments = new ArrayList<>();

    @Column
    private Long score;
    
    @Column(name="create_time")
    private LocalDateTime createTime;

    @Column
    private String title;
    
    @Column
    private String url;
       
    @Column(name="data_fetched_at")
    private LocalDateTime dataFetchedAt;

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

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
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

	public LocalDateTime getDataFetchedAt() {
		return dataFetchedAt;
	}

	public void setDataFetchedAt(LocalDateTime dataFetchedAt) {
		this.dataFetchedAt = dataFetchedAt;
	}

}
