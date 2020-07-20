package com.interview.questionpro.hackernews.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	
	@Id
	private String id;
    
    @Column(name="create_time")
    private LocalDateTime createTime;
    
    @Column(name="data_fetched_at")
    private LocalDateTime dataFetchedAt;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getDataFetchedAt() {
		return dataFetchedAt;
	}

	public void setDataFetchedAt(LocalDateTime dataFetchedAt) {
		this.dataFetchedAt = dataFetchedAt;
	}
    	
}
