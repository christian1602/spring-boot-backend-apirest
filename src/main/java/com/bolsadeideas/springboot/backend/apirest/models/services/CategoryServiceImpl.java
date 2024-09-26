package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.dao.ICategoryDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Category;

@Service
public class CategoryServiceImpl implements ICategoryService {

	private final ICategoryDao categoryDao;

	public CategoryServiceImpl(ICategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Category> findAll() {
		return (List<Category>) this.categoryDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Category findById(Long id) {
		return this.categoryDao.findById(id).orElse(null);
	}

	@Override
	@Transactional()
	public Category save(Category category) {
		return this.categoryDao.save(category);
	}

	@Override
	@Transactional()
	public void delete(Long id) {
		this.categoryDao.deleteById(id);
	}
}
