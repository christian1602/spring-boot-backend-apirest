package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductCategoryReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductCategoryWriteDTO;

public interface IProductCategoryService {
	List<ProductCategoryReadDTO> findAll();
	ProductCategoryReadDTO findById(Long id);
	ProductCategoryReadDTO save(ProductCategoryWriteDTO productCategoryWriteDTO);
	ProductCategoryReadDTO update(Long id, ProductCategoryWriteDTO productCategoryWriteDTO);
	void delete(Long id);
}
