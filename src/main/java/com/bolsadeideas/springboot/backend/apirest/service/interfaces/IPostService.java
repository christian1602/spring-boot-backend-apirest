package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostWriteDTO;

public interface IPostService {

	List<PostReadDTO> findAll();
	PostReadDTO findById(Long id);
	PostReadDTO save(PostWriteDTO postWriteDTO);
	PostReadDTO update(Long id, PostWriteDTO postWriteDTO);
	void delete(Long id);
	// List<PostDto> findAllPostsWithUserId();
}
