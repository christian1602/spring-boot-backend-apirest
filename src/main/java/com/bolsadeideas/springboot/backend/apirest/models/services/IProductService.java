package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Product;

public interface IProductService {
	
	List<Product> findAll();
	Product findById(Long id);
	Product save(Product product);
	void delete(Long id);
}
