package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadWithUserIdDTO;

public interface IPostCustomDao {
	List<PostReadWithUserIdDTO> findAllPostsWithUserId();
}
