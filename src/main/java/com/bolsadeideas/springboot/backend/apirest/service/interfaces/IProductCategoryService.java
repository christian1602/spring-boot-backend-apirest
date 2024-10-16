package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductCategory;

public interface IProductCategoryService {
	List<ProductCategory> findAll();
	ProductCategory findById(Long id);
	ProductCategory save(ProductCategory productCategory);
	void delete(Long id);
}
