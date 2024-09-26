package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Category;

public interface ICategoryService {
	List<Category> findAll();
	Category findById(Long id);
	Category save(Category category);
	void delete(Long id);
}
