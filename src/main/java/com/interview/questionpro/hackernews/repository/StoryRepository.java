package com.interview.questionpro.hackernews.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.interview.questionpro.hackernews.model.Story;

@Repository
public interface StoryRepository  extends JpaRepository<Story, Long> {
	
	@Query("select s from Story s join fetch s.comments where s.id = :id")
	Optional<Story> findWithCommentsById(@Param("id")Long id);
	
	@Query("select s from Story s where s.dataFetchedAt >= :dataFetchedAt")
	List<Story> findByDataFetchedAtGreaterThan(@Param("dataFetchedAt")LocalDateTime dataFetchedAfter, Pageable pageable);
}
