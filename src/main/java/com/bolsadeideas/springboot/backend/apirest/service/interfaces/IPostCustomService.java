package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadWithUserIdDTO;

public interface IPostCustomService {

	List<PostReadWithUserIdDTO> getAllPostsWithUserId();
}
