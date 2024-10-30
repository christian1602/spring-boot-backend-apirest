package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryWriteDTO;

public interface ICategoryService {
	List<CategoryReadDTO> findAll();
	CategoryReadDTO findById(Long id);
	CategoryReadDTO save(CategoryWriteDTO categoryWriteDTO);
	CategoryReadDTO update(Long id, CategoryWriteDTO categoryWriteDTO);
	void delete(Long id);
}
