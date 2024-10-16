package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PostEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IPostRepository;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostApiService;

@Service
public class PostApiServiceImpl implements IPostApiService {
	
	private final IPostRepository postDao;
	private final RestTemplate restTemplate;
	
	@Value("${external.api.url.posts}")
	private String postApiUrl;

	public PostApiServiceImpl(IPostRepository postDao, RestTemplate restTemplate) {
		this.postDao = postDao;
		this.restTemplate = restTemplate;
	}
	
	@Override
	public List<PostEntity> fetchPosts() {
		PostEntity[] postArray = this.restTemplate.getForObject(postApiUrl, PostEntity[].class); 
		return Arrays.asList(postArray);
	}
	
	@Override
	@Transactional()
	public void saveAll(List<PostEntity> posts) {
		this.postDao.saveAll(posts);
	}

}
