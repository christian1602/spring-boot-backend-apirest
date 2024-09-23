package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Post;

public interface IPostApiService {
		
	List<Post> fetchPosts();
	void saveAll(List<Post> posts);
}
