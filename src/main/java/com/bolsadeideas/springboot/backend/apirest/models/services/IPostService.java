package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Post;

public interface IPostService {

	List<Post> findAll();
	Post findById(Long id);
	Post save(Post post);
	void delete(Long id);
	// List<PostDto> findAllPostsWithUserId();
}
