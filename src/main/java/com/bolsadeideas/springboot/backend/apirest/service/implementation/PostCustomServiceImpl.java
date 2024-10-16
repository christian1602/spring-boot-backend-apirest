package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IPostCustomDao;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostCustomService;

@Service
public class PostCustomServiceImpl implements IPostCustomService {
	
	private final IPostCustomDao postCustomDao; 
	
	public PostCustomServiceImpl(IPostCustomDao postCustomDao) {		
		this.postCustomDao = postCustomDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PostDTO> getAllPostsWithUserId() {
		return this.postCustomDao.findAllPostsWithUserId();
	}
}
