package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IPostRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostApiService;

import jakarta.annotation.PostConstruct;

@Service
public class PostApiServiceImpl implements IPostApiService {	
	private final IPostRepository postDao;
	private final RestTemplate restTemplate;
	private WebClient webClient;
	
	@Value("${external.api.url.posts}")
	private String postApiUrl;

	public PostApiServiceImpl(IPostRepository postDao, RestTemplate restTemplate) {
		this.postDao = postDao;
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
	public List<PostDTO> fetchPostsRestTemplate() {
		PostDTO[] postArray = this.restTemplate.getForObject(this.postApiUrl.concat("/posts"), PostDTO[].class);
		return Arrays.asList(postArray);
	}
	
	@Override
	public List<PostDTO> fetchPostsWebClient() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return this.webClient.get()
				.uri("/posts")
				.retrieve()
				.bodyToFlux(PostDTO.class)
				.collectList()
				.contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
				.block();
	}
	
	@Override
	@Transactional()
	public void saveAll(List<PostDTO> posts) {
		// this.postDao.saveAll(posts);
	}
}
