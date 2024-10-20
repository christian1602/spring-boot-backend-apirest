package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		Optional<ProductEntity> productEntityOptional = this.productoRepository.findById(id); 
		return productEntityOptional.map(this.productMapper::productEntityToProductDTO).orElse(null);
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
	public void delete(Long id) {
		this.productoRepository.deleteById(id);
	}
}
