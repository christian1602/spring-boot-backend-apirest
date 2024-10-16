package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostDTO;

public interface IPostCustomService {

	List<PostDTO> getAllPostsWithUserId();
}
