package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostDTO;

public interface IPostService {

	List<PostDTO> findAll();
	PostDTO findById(Long id);
	PostDTO save(PostDTO postDTO);
	PostDTO update(Long id, PostDTO postDTO);
	void delete(Long id);
	// List<PostDto> findAllPostsWithUserId();
}
