package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductWriteDTO;

public interface IProductService {
	
	List<ProductReadDTO> findAll();
	ProductReadDTO findById(Long id);
	ProductReadDTO save(ProductWriteDTO productWriteDTO);
	ProductReadDTO update(Long id, ProductWriteDTO productWriteDTO);
	void delete(Long id);
}
