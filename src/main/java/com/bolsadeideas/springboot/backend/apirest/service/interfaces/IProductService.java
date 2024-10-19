package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductDTO;

public interface IProductService {
	
	List<ProductDTO> findAll();
	ProductDTO findById(Long id);
	ProductDTO save(ProductDTO productDTO);
	void delete(Long id);
}
