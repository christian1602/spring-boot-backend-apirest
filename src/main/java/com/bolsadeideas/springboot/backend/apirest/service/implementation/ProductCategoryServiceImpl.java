package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.mappers.ProductCategoryMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.CategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductCategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.ICategoryRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IProductCategoryRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IProductRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductCategoryDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements IProductCategoryService {

	private final IProductCategoryRepository productCategoryRepository;
	private final IProductRepository productRepository;
	private final ICategoryRepository categoryRepository;
	private final ProductCategoryMapper productCategoryMapper;

	public ProductCategoryServiceImpl(
			IProductCategoryRepository productCategoryRepository,
			IProductRepository productRepository, 
			ICategoryRepository categoryRepository,
			ProductCategoryMapper productCategoryMapper) {
		this.productCategoryRepository = productCategoryRepository;
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.productCategoryMapper = productCategoryMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategoryDTO> findAll() {
		Iterable<ProductCategoryEntity> productCategories = this.productCategoryRepository.findAll();
		return StreamSupport.stream(productCategories.spliterator(), false)
				.map(this.productCategoryMapper::productCategoryEntityToProductCategoryDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ProductCategoryDTO findById(Long id) {
		Optional<ProductCategoryEntity> ProductCategoryOptional = this.productCategoryRepository.findById(id);
		return ProductCategoryOptional.map(this.productCategoryMapper::productCategoryEntityToProductCategoryDTO)
				.orElse(null);
	}

	@Override
	@Transactional
	public ProductCategoryDTO save(ProductCategoryDTO productCategoryDTO) {
		ProductCategoryEntity ProductCategoryEntity = this.productCategoryMapper.productCategoryDTOToProductCategoryEntity(productCategoryDTO);
		
		Optional<ProductEntity> productEntityOptional = this.productRepository.findById(productCategoryDTO.getProductId());
		
		if (productEntityOptional.isEmpty()) {
			return null;
		}
		
		Optional<CategoryEntity> categoryEntityOptional = this.categoryRepository.findById(productCategoryDTO.getCategoryId());
		
		if (categoryEntityOptional.isEmpty()) {
			return null;
		}
		
		ProductCategoryEntity.setProduct(productEntityOptional.get());
		ProductCategoryEntity.setCategory(categoryEntityOptional.get());
		
		ProductCategoryEntity productCategoryEntitySaved = this.productCategoryRepository.save(ProductCategoryEntity); 
		
		return this.productCategoryMapper.productCategoryEntityToProductCategoryDTO(productCategoryEntitySaved);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.productCategoryRepository.deleteById(id);
	}
}
