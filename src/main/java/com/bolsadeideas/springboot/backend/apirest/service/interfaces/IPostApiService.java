package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadWithUserIdDTO;

public interface IPostApiService {
		
	List<PostReadWithUserIdDTO> apiFetchPostsRestTemplate();
	List<PostReadWithUserIdDTO> apiFetchPostsWebClient();
	PostReadWithUserIdDTO apiFindById(Long id);
	// void saveAll(List<PostDTO> posts);
}
