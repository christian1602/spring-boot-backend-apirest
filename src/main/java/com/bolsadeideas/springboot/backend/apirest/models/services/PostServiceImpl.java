package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IPostDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Post;

@Service
public class PostServiceImpl implements IPostService {

	private final IPostDao postDao;

	public PostServiceImpl(IPostDao postDao) {
		this.postDao = postDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Post> findAll() {
		return (List<Post>) this.postDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Post findById(Long id) {
		return this.postDao.findById(id).orElseGet(null);
	}

	@Override
	@Transactional
	public Post save(Post post) {
		return this.postDao.save(post);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.postDao.deleteById(id);
	}
}
