package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductCategoryDTO;

public interface IProductCategoryService {
	List<ProductCategoryDTO> findAll();
	ProductCategoryDTO findById(Long id);
	ProductCategoryDTO save(ProductCategoryDTO productCategoryDTO);
	ProductCategoryDTO update(Long id, ProductCategoryDTO productCategoryDTO);
	void delete(Long id);
}
