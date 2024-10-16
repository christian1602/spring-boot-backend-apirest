package com.bolsadeideas.springboot.backend.apirest.persistence.repository;


import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PostEntity;

public interface IPostRepository extends CrudRepository<PostEntity, Long> {
	
	// @Query("SELECT new com.bolsadeideas.springboot.backend.apirest.dto.PostDto(p.id, p.title, p.body, u.id) FROM Post p JOIN p.user u")
	// List<PostDto> findAllPostsWithUserId();
}
