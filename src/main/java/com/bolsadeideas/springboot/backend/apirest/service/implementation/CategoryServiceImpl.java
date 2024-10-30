package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exception.CategoryAlreadyExistsInProductCategoryException;
import com.bolsadeideas.springboot.backend.apirest.exception.CategoryNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.mappers.CategoryReadMapper;
import com.bolsadeideas.springboot.backend.apirest.mappers.CategoryWriteMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.CategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.ICategoryRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.ICategoryService;

@Service
public class CategoryServiceImpl implements ICategoryService {

	private final ICategoryRepository categoryRepository;
	private final CategoryReadMapper categoryReadMapper;
	private final CategoryWriteMapper categoryWriteMapper;

	public CategoryServiceImpl(
			ICategoryRepository categoryRepository, 
			CategoryReadMapper categoryReadMapper, 
			CategoryWriteMapper categoryWriteMapper) {
		this.categoryRepository = categoryRepository;
		this.categoryReadMapper = categoryReadMapper;
		this.categoryWriteMapper = categoryWriteMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryReadDTO> findAll() {		
		Iterable<CategoryEntity> categories = this.categoryRepository.findAll();		
		return StreamSupport.stream(categories.spliterator(), false)
				.map(this.categoryReadMapper::toCategoryReadDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryReadDTO findById(Long id) {
		CategoryEntity categoryEntity = this.categoryRepository.findById(id)
			.orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: ".concat(id.toString()))); 
		return this.categoryReadMapper.toCategoryReadDTO(categoryEntity);
	}

	@Override
	@Transactional
	public CategoryReadDTO save(CategoryWriteDTO categoryWriteDTO) {
		CategoryEntity categoryEntity = this.categoryWriteMapper.toCategoryEntity(categoryWriteDTO);
		CategoryEntity savedCategoryEntity = this.categoryRepository.save(categoryEntity); 
		return this.categoryReadMapper.toCategoryReadDTO(savedCategoryEntity);
	}
	
	@Override
	@Transactional
	public CategoryReadDTO update(Long id, CategoryWriteDTO categoryWriteDTO) {
		CategoryEntity existingCategoryEntity = this.categoryRepository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: ".concat(id.toString())));
		
		existingCategoryEntity.setName(categoryWriteDTO.name());
		
		CategoryEntity updatedCategoryEntity = this.categoryRepository.save(existingCategoryEntity);
		return this.categoryReadMapper.toCategoryReadDTO(updatedCategoryEntity);
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
