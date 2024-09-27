package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.dto.PostDto;
import com.bolsadeideas.springboot.backend.apirest.models.dao.IPostCustomDao;

@Service
public class PostCustomServiceImpl implements IPostCustomService {
	
	private final IPostCustomDao postCustomDao; 
	
	public PostCustomServiceImpl(IPostCustomDao postCustomDao) {		
		this.postCustomDao = postCustomDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PostDto> getAllPostsWithUserId() {
		return this.postCustomDao.findAllPostsWithUserId();
	}
}
