package com.bolsadeideas.springboot.backend.apirest.models.dao;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.dto.PostDto;

public interface IPostCustomDao {
	List<PostDto> findAllPostsWithUserId();
}
