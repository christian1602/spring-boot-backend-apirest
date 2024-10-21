package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exceptions.CategoryAlreadyExistsInProductCategoryException;
import com.bolsadeideas.springboot.backend.apirest.exceptions.CategoryNotFoundException;
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
		CategoryEntity categoryEntity = this.categoryRepository.findById(id)
			.orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: ".concat(id.toString()))); 
		return this.categoryMapper.categoryEntityToCategoryDTO(categoryEntity);
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
	public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
		CategoryEntity existingCategoryEntity = this.categoryRepository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: ".concat(id.toString())));
		
		existingCategoryEntity.setName(categoryDTO.getName());
		
		CategoryEntity updatedCategoryEntity = this.categoryRepository.save(existingCategoryEntity);
		return this.categoryMapper.categoryEntityToCategoryDTO(updatedCategoryEntity);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.categoryRepository.findById(id)
			.orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: ".concat(id.toString())));
		
		if (this.categoryRepository.existsByProductCategoriesCategoryId(id)) {
			throw new CategoryAlreadyExistsInProductCategoryException("Category with ID: ".concat(id.toString()).concat(" has products"));
		}

		this.categoryRepository.deleteById(id);
	}
}
