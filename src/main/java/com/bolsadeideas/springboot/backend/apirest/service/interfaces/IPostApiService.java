package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PostEntity;

public interface IPostApiService {
		
	List<PostEntity> fetchPosts();
	void saveAll(List<PostEntity> posts);
}
