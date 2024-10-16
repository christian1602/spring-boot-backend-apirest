package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductEntity;

public interface IProductService {
	
	List<ProductEntity> findAll();
	ProductEntity findById(Long id);
	ProductEntity save(ProductEntity product);
	void delete(Long id);
}
