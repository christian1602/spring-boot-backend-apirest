package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.mappers.CategoryMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.CategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.ICategoryRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.ICategoryService;

@Service
public class CategoryServiceImpl implements ICategoryService {

	private final ICategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	public CategoryServiceImpl(ICategoryRepository categoryRepository, CategoryMapper categoryMapper) {
		this.categoryRepository = categoryRepository;
		this.categoryMapper = categoryMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {		
		Iterable<CategoryEntity> categories = this.categoryRepository.findAll();
		
		return StreamSupport.stream(categories.spliterator(), false)
				.map(this.categoryMapper::categoryEntityToCategoryDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<CategoryEntity> categoryEntityOptional = this.categoryRepository.findById(id); 
		return categoryEntityOptional.map(this.categoryMapper::categoryEntityToCategoryDTO).orElse(null);
	}

	@Override
	@Transactional
	public CategoryDTO save(CategoryDTO categoryDTO) {
		CategoryEntity categoryEntity = this.categoryMapper.categoryDTOToCategoryEntity(categoryDTO);
		CategoryEntity categoryEntitySaved = this.categoryRepository.save(categoryEntity); 
		return this.categoryMapper.categoryEntityToCategoryDTO(categoryEntitySaved);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.categoryRepository.deleteById(id);
	}
}
