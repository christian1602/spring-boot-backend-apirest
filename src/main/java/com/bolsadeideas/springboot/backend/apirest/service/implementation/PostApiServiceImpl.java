package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IPostRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadWithUserIdDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostApiService;

import jakarta.annotation.PostConstruct;

@Service
public class PostApiServiceImpl implements IPostApiService {	
	private final IPostRepository postRepository;
	private final RestTemplate restTemplate;
	private WebClient webClient;
	
	@Value("${external.api.url.posts}")
	private String postApiUrl;

	public PostApiServiceImpl(IPostRepository postRepository, RestTemplate restTemplate) {
		this.postRepository = postRepository;
		this.restTemplate = restTemplate;
	}
	
	// SOLO PARA INICIALIZAR webClient
	@PostConstruct
	public void init() {
		this.webClient = WebClient.builder()
				 .baseUrl(this.postApiUrl)
				 .build();
	}
	
	@Override
	public List<PostReadWithUserIdDTO> apiFetchPostsRestTemplate() {
		String url = this.postApiUrl.concat("/posts/");
		PostReadWithUserIdDTO[] postReadWithUserIdDTOArray = this.restTemplate.getForObject(url, PostReadWithUserIdDTO[].class);

		if (postReadWithUserIdDTOArray != null) {
            return Arrays.asList(postReadWithUserIdDTOArray);
        }

		return Collections.emptyList();
    }
	
	@Override
	public List<PostReadWithUserIdDTO> apiFetchPostsWebClient() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return this.webClient.get()
				.uri("/posts")
				.retrieve()
				.bodyToFlux(PostReadWithUserIdDTO.class)
				.collectList()
				.contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
				.block();
	}
	
	@Override
	public PostReadWithUserIdDTO apiFindById(Long id) {
		String url = this.postApiUrl.concat("/posts/").concat(id.toString());
		return this.restTemplate.getForObject(url,PostReadWithUserIdDTO.class);		
	}
	
	/*
	@Override
	@Transactional()
	public void saveAll(List<PostDTO> posts) {
		this.postDao.saveAll(posts);
	}
	*/
}
