package com.interview.questionpro.hackernews.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.interview.questionpro.hackernews.model.Comment;
import com.interview.questionpro.hackernews.model.Story;

@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long> {
	
	@Query("select c from Comment c join fetch c.author where c.parentStory = :parentStory and c.deleted = :deleted")
	List<Comment> findByParentStoryAndDeleted(@Param("parentStory")Story story, @Param("deleted")boolean deleted, Pageable pageable);
}
