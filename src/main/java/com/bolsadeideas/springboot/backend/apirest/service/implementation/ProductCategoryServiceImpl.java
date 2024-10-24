package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exception.CategoryNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.exception.ProductAndCategoryAlreadyExistsInProductCategoryException;
import com.bolsadeideas.springboot.backend.apirest.exception.ProductCategoryNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.exception.ProductNotFoundException;
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
		ProductCategoryEntity productCategoryEntity = this.productCategoryRepository.findById(id)
				.orElseThrow(() -> new ProductCategoryNotFoundException("Product and Category not found with ID: ".concat(id.toString())));				
		return this.productCategoryMapper.productCategoryEntityToProductCategoryDTO(productCategoryEntity);
	}

	@Override
	@Transactional
	public ProductCategoryDTO save(ProductCategoryDTO productCategoryDTO) {
		ProductCategoryEntity productCategoryEntity = this.productCategoryMapper.productCategoryDTOToProductCategoryEntity(productCategoryDTO);
		
		ProductEntity productEntity = this.productRepository.findById(productCategoryDTO.productId())
				.orElseThrow(()-> new ProductNotFoundException("Product not found with ID: ".concat(productCategoryDTO.productId().toString())));
		
		CategoryEntity categoryEntity = this.categoryRepository.findById(productCategoryDTO.categoryId())
				.orElseThrow(()-> new CategoryNotFoundException("Category not found with ID: ".concat(productCategoryDTO.categoryId().toString())));
		
		Optional<ProductCategoryEntity> productCategoryEntityOptional = this.productCategoryRepository.findByProductIdAndCategoryId(productCategoryDTO.productId(),productCategoryDTO.categoryId());
		
		if (productCategoryEntityOptional.isPresent()) {
			throw new ProductAndCategoryAlreadyExistsInProductCategoryException("Product with ID: "
					.concat(productCategoryDTO.productId().toString())
					.concat(" and Category with ID: ")
					.concat(productCategoryDTO.categoryId().toString())
					.concat(" exists"));	
		}			
				
		productCategoryEntity.setProduct(productEntity);
		productCategoryEntity.setCategory(categoryEntity);
		
		ProductCategoryEntity productCategoryEntitySaved = this.productCategoryRepository.save(productCategoryEntity); 
		
		return this.productCategoryMapper.productCategoryEntityToProductCategoryDTO(productCategoryEntitySaved);
	}
	
	@Override
	@Transactional
	public ProductCategoryDTO update(Long id, ProductCategoryDTO productCategoryDTO) {
		ProductCategoryEntity existingProductCategoryEntity = this.productCategoryRepository.findById(id)
				.orElseThrow(() -> new ProductCategoryNotFoundException("Product and Category not found with ID: ".concat(id.toString())));
		
		ProductEntity productEntity = this.productRepository.findById(productCategoryDTO.productId())
				.orElseThrow(()-> new ProductNotFoundException("Product not found with ID: ".concat(productCategoryDTO.productId().toString())));
		
		CategoryEntity categoryEntity = this.categoryRepository.findById(productCategoryDTO.categoryId())
				.orElseThrow(()-> new CategoryNotFoundException("Category not found with ID: ".concat(productCategoryDTO.categoryId().toString())));		
		
		// Si la combinación de product_id y category_id cambio
		if (!existingProductCategoryEntity.getProduct().getId().equals(productCategoryDTO.productId()) || 
			!existingProductCategoryEntity.getCategory().getId().equals(productCategoryDTO.categoryId())) {
						
			// Validar si la nueva combinación ya existe en otro registro
			Optional<ProductCategoryEntity> existingOptional = this.productCategoryRepository.findByProductIdAndCategoryId(productCategoryDTO.productId(),productCategoryDTO.categoryId());
			
			if (existingOptional.isPresent() && !existingOptional.get().getId().equals(id)) {			
				throw new ProductAndCategoryAlreadyExistsInProductCategoryException("Product with ID: "
						.concat(productCategoryDTO.productId().toString())
						.concat(" and Category with ID: ")
						.concat(productCategoryDTO.categoryId().toString())
						.concat(" exists"));
			}
		}
		
		existingProductCategoryEntity.setDescription(productCategoryDTO.description());
		existingProductCategoryEntity.setProduct(productEntity);
		existingProductCategoryEntity.setCategory(categoryEntity);
		
		ProductCategoryEntity updatedProductCategoryEntity = this.productCategoryRepository.save(existingProductCategoryEntity);
		
		return this.productCategoryMapper.productCategoryEntityToProductCategoryDTO(updatedProductCategoryEntity);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.productCategoryRepository.findById(id)
			.orElseThrow(() -> new ProductCategoryNotFoundException("Product and Category not found with ID: ".concat(id.toString())));
		
		this.productCategoryRepository.deleteById(id);
	}
}
