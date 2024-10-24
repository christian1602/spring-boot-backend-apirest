package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exception.ProductAlreadyExistsInProductCategoryException;
import com.bolsadeideas.springboot.backend.apirest.exception.ProductNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.mappers.ProductMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IProductRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProductService;

@Service
public class ProductServiceImpl implements IProductService {

	private final IProductRepository productoRepository;
	private final ProductMapper productMapper;

	public ProductServiceImpl(IProductRepository productoDao, ProductMapper productMapper) {
		this.productoRepository = productoDao;
		this.productMapper = productMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductDTO> findAll() {
		Iterable<ProductEntity> products = this.productoRepository.findAll(); 
		return StreamSupport.stream(products.spliterator(), false)
				.map(this.productMapper::productEntityToProductDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		ProductEntity productEntity = this.productoRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with ID: ".concat(id.toString()))); 
		return this.productMapper.productEntityToProductDTO(productEntity);
	}

	@Override
	@Transactional
	public ProductDTO save(ProductDTO productDTO) {
		ProductEntity productEntity = this.productMapper.productDTOToProductEntity(productDTO);
		ProductEntity productEntitySaved = this.productoRepository.save(productEntity);
		return this.productMapper.productEntityToProductDTO(productEntitySaved);
	}
	
	@Override
	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {		
		ProductEntity existingProductEntity = this.productoRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with ID: ".concat(id.toString())));
		
		existingProductEntity.setName(productDTO.name());

		ProductEntity updatedProductEntity = this.productoRepository.save(existingProductEntity);

		return this.productMapper.productEntityToProductDTO(updatedProductEntity);
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
