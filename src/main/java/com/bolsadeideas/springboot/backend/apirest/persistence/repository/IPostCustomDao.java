package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostDTO;

public interface IPostCustomDao {
	List<PostDTO> findAllPostsWithUserId();
}
