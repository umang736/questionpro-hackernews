package com.interview.questionpro.hackernews.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.interview.questionpro.hackernews.model.User;

@Repository
public interface UserRepository  extends JpaRepository<User, String> {

//	@Query("select id from User where id in (:ids)")
//	List<String> findIdByIdIn(@Param("ids") List<String> ids);

	List<User> findByIdIn(List<String> ids);
}
