package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostDTO;

public interface IPostApiService {
		
	List<PostDTO> apiFetchPostsRestTemplate();
	List<PostDTO> apiFetchPostsWebClient();
	PostDTO apiFindById(Long id);
	void saveAll(List<PostDTO> posts);
}
