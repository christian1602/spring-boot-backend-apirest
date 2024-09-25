package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.models.entity.ProductCategory;

public interface IProductCategoryService {
	List<ProductCategory> findAll();
	ProductCategory findById(Long id);
	ProductCategory save(ProductCategory productCategory);
	void delete(Long id);
}
