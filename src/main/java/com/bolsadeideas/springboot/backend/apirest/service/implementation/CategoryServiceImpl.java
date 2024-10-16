package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.CategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.ICategoryRepository;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.ICategoryService;

@Service
public class CategoryServiceImpl implements ICategoryService {

	private final ICategoryRepository categoryDao;

	public CategoryServiceImpl(ICategoryRepository categoryDao) {
		this.categoryDao = categoryDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryEntity> findAll() {
		return (List<CategoryEntity>) this.categoryDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryEntity findById(Long id) {
		return this.categoryDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public CategoryEntity save(CategoryEntity category) {
		return this.categoryDao.save(category);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.categoryDao.deleteById(id);
	}
}
