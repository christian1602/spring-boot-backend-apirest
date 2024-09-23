package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IPostDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Post;

@Service
public class PostApiServiceImpl implements IPostApiService {
	
	private final IPostDao postDao;
	private final RestTemplate restTemplate;
	
	@Value("${external.api.url.posts}")
	private String postApiUrl;

	public PostApiServiceImpl(IPostDao postDao, RestTemplate restTemplate) {
		this.postDao = postDao;
		this.restTemplate = restTemplate;
	}
	
	@Override
	public List<Post> fetchPosts() {
		Post[] postArray = this.restTemplate.getForObject(postApiUrl, Post[].class); 
		return Arrays.asList(postArray);
	}
	
	@Override
	@Transactional()
	public void saveAll(List<Post> posts) {
		this.postDao.saveAll(posts);
	}

}
