package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exception.ProductAlreadyExistsInProductCategoryException;
import com.bolsadeideas.springboot.backend.apirest.exception.ProductNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.mappers.ProductReadMapper;
import com.bolsadeideas.springboot.backend.apirest.mappers.ProductWriteMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IProductRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProductService;

@Service
public class ProductServiceImpl implements IProductService {

	private final IProductRepository productoRepository;
	private final ProductReadMapper productReadMapper;
	private final ProductWriteMapper productWriteMapper;

	public ProductServiceImpl(
			IProductRepository productoDao, 
			ProductReadMapper productReadMapper, 
			ProductWriteMapper productWriteMapper) {
		this.productoRepository = productoDao;
		this.productReadMapper = productReadMapper;
		this.productWriteMapper = productWriteMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductReadDTO> findAll() {
		Iterable<ProductEntity> products = this.productoRepository.findAll(); 
		return StreamSupport.stream(products.spliterator(), false)
				.map(this.productReadMapper::toProductReadDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ProductReadDTO findById(Long id) {
		ProductEntity productEntity = this.productoRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with ID: ".concat(id.toString()))); 
		return this.productReadMapper.toProductReadDTO(productEntity);
	}

	@Override
	@Transactional
	public ProductReadDTO save(ProductWriteDTO productWriteDTO) {
		ProductEntity productEntity = this.productWriteMapper.toProductEntity(productWriteDTO);
		ProductEntity savedProductEntity = this.productoRepository.save(productEntity);
		return this.productReadMapper.toProductReadDTO(savedProductEntity);
	}
	
	@Override
	@Transactional
	public ProductReadDTO update(Long id, ProductWriteDTO productWriteDTO) {		
		ProductEntity existingProductEntity = this.productoRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with ID: ".concat(id.toString())));
		
		existingProductEntity.setName(productWriteDTO.name());

		ProductEntity updatedProductEntity = this.productoRepository.save(existingProductEntity);

		return this.productReadMapper.toProductReadDTO(updatedProductEntity);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.productoRepository.findById(id)
			.orElseThrow(() -> new ProductNotFoundException("Product not found with ID: ".concat(id.toString())));
		
		if (this.productoRepository.existsByProductCategoriesProductId(id)) {
			throw new ProductAlreadyExistsInProductCategoryException("Product with ID: ".concat(id.toString()).concat(" has categories"));
		}
			
		this.productoRepository.deleteById(id);
	}
}
