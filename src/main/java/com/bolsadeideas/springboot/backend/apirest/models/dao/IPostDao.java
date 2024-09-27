package com.bolsadeideas.springboot.backend.apirest.models.dao;


import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Post;

public interface IPostDao extends CrudRepository<Post, Long> {
	
	// @Query("SELECT new com.bolsadeideas.springboot.backend.apirest.dto.PostDto(p.id, p.title, p.body, u.id) FROM Post p JOIN p.user u")
	// List<PostDto> findAllPostsWithUserId();
}
