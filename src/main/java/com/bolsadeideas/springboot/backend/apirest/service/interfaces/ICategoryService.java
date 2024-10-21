package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryDTO;

public interface ICategoryService {
	List<CategoryDTO> findAll();
	CategoryDTO findById(Long id);
	CategoryDTO save(CategoryDTO categoryDTO);
	CategoryDTO update(Long id, CategoryDTO categoryDTO);
	void delete(Long id);
}
