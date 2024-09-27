package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.dto.PostDto;

public interface IPostCustomService {

	List<PostDto> getAllPostsWithUserId();
}
