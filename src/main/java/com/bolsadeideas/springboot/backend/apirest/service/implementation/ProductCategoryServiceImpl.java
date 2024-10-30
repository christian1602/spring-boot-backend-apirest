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
import com.bolsadeideas.springboot.backend.apirest.mappers.ProductCategoryReadMapper;
import com.bolsadeideas.springboot.backend.apirest.mappers.ProductCategoryWriteMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.CategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductCategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.ICategoryRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IProductCategoryRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IProductRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductCategoryReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductCategoryWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements IProductCategoryService {

	private final IProductCategoryRepository productCategoryRepository;
	private final IProductRepository productRepository;
	private final ICategoryRepository categoryRepository;
	private final ProductCategoryReadMapper productCategoryReadMapper;
	private final ProductCategoryWriteMapper productCategoryWriteMapper;

	public ProductCategoryServiceImpl(
			IProductCategoryRepository productCategoryRepository,
			IProductRepository productRepository, 
			ICategoryRepository categoryRepository,
			ProductCategoryReadMapper productCategoryReadMapper,
			ProductCategoryWriteMapper productCategoryWriteMapper
			) {
		this.productCategoryRepository = productCategoryRepository;
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.productCategoryReadMapper = productCategoryReadMapper;
		this.productCategoryWriteMapper = productCategoryWriteMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategoryReadDTO> findAll() {
		Iterable<ProductCategoryEntity> productCategories = this.productCategoryRepository.findAll();
		return StreamSupport.stream(productCategories.spliterator(), false)
				.map(this.productCategoryReadMapper::toProductCategoryReadDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ProductCategoryReadDTO findById(Long id) {
		ProductCategoryEntity productCategoryEntity = this.productCategoryRepository.findById(id)
				.orElseThrow(() -> new ProductCategoryNotFoundException("Product and Category not found with ID: ".concat(id.toString())));				
		return this.productCategoryReadMapper.toProductCategoryReadDTO(productCategoryEntity);
	}

	@Override
	@Transactional
	public ProductCategoryReadDTO save(ProductCategoryWriteDTO productCategoryWriteDTO) {		
		ProductEntity productEntity = this.productRepository.findById(productCategoryWriteDTO.productId())
				.orElseThrow(()-> new ProductNotFoundException("Product not found with ID: ".concat(productCategoryWriteDTO.productId().toString())));
		
		CategoryEntity categoryEntity = this.categoryRepository.findById(productCategoryWriteDTO.categoryId())
				.orElseThrow(()-> new CategoryNotFoundException("Category not found with ID: ".concat(productCategoryWriteDTO.categoryId().toString())));		
		
		this.validateIfAlreadyExistsInProductCategory(productCategoryWriteDTO);
		
		ProductCategoryEntity productCategoryEntity = this.productCategoryWriteMapper.toProductCategoryEntity(productCategoryWriteDTO);		
		productCategoryEntity.setProduct(productEntity);
		productCategoryEntity.setCategory(categoryEntity);
		ProductCategoryEntity savedProductCategoryEntity = this.productCategoryRepository.save(productCategoryEntity); 
		
		return this.productCategoryReadMapper.toProductCategoryReadDTO(savedProductCategoryEntity);
	}
	
	@Override
	@Transactional
	public ProductCategoryReadDTO update(Long id, ProductCategoryWriteDTO productCategoryWriteDTO) {
		ProductCategoryEntity existingProductCategoryEntity = this.productCategoryRepository.findById(id)
				.orElseThrow(() -> new ProductCategoryNotFoundException("Product and Category not found with ID: ".concat(id.toString())));
		
		ProductEntity productEntity = this.productRepository.findById(productCategoryWriteDTO.productId())
				.orElseThrow(()-> new ProductNotFoundException("Product not found with ID: ".concat(productCategoryWriteDTO.productId().toString())));
		
		CategoryEntity categoryEntity = this.categoryRepository.findById(productCategoryWriteDTO.categoryId())
				.orElseThrow(()-> new CategoryNotFoundException("Category not found with ID: ".concat(productCategoryWriteDTO.categoryId().toString())));		
		
		this.validateIfIfTheNewCombinationAlreadyExistsInOtherRecords(id, existingProductCategoryEntity, productCategoryWriteDTO);
		
		existingProductCategoryEntity.setDescription(productCategoryWriteDTO.description());
		existingProductCategoryEntity.setProduct(productEntity);
		existingProductCategoryEntity.setCategory(categoryEntity);
		
		ProductCategoryEntity updatedProductCategoryEntity = this.productCategoryRepository.save(existingProductCategoryEntity);
		
		return this.productCategoryReadMapper.toProductCategoryReadDTO(updatedProductCategoryEntity);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.productCategoryRepository.findById(id)
			.orElseThrow(() -> new ProductCategoryNotFoundException("Product and Category not found with ID: ".concat(id.toString())));
		
		this.productCategoryRepository.deleteById(id);
	}
	
	private void validateIfAlreadyExistsInProductCategory(ProductCategoryWriteDTO productCategoryWriteDTO) {
		Optional<ProductCategoryEntity> productCategoryEntityOptional = this.productCategoryRepository
				.findByProductIdAndCategoryId(productCategoryWriteDTO.productId(),productCategoryWriteDTO.categoryId());		
		
		if (productCategoryEntityOptional.isPresent()) {
			throw new ProductAndCategoryAlreadyExistsInProductCategoryException("Product with ID: "
					.concat(productCategoryWriteDTO.productId().toString())
					.concat(" and Category with ID: ")
					.concat(productCategoryWriteDTO.categoryId().toString())
					.concat(" exists"));
		}
	}
	
	private void validateIfIfTheNewCombinationAlreadyExistsInOtherRecords(Long id,ProductCategoryEntity existingProductCategoryEntity,ProductCategoryWriteDTO productCategoryWriteDTO) {
		// SI LA COMBINACION DE product_id y category_id CAMBIO
		if (!existingProductCategoryEntity.getProduct().getId().equals(productCategoryWriteDTO.productId()) || 
			!existingProductCategoryEntity.getCategory().getId().equals(productCategoryWriteDTO.categoryId())) {

			// VALIDAR SI LA NUEVA COMBINACION YA ESITE EN OTROS REGISTROS
			Optional<ProductCategoryEntity> existingOptional = this.productCategoryRepository
					.findByProductIdAndCategoryId(productCategoryWriteDTO.productId(),productCategoryWriteDTO.categoryId());
			
			if (existingOptional.isPresent() && !existingOptional.get().getId().equals(id)) {			
				throw new ProductAndCategoryAlreadyExistsInProductCategoryException("Product with ID: "
						.concat(productCategoryWriteDTO.productId().toString())
						.concat(" and Category with ID: ")
						.concat(productCategoryWriteDTO.categoryId().toString())
						.concat(" exists"));
			}
		}
	}
}
