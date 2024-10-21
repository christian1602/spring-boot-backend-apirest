package com.bolsadeideas.springboot.backend.apirest.presentation.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.exceptions.InvalidDataException;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductCategoryDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProductCategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" })
public class ProductCategoryRestController {

	private final IProductCategoryService productCategoryService;	

	public ProductCategoryRestController(IProductCategoryService productCategoryService) {
		this.productCategoryService = productCategoryService;
	}

	@GetMapping("/product_category")
	public List<ProductCategoryDTO> index() {
		return this.productCategoryService.findAll();
	}

	@GetMapping("/product_category/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		ProductCategoryDTO productCategoryDTO = this.productCategoryService.findById(id);
		return new ResponseEntity<ProductCategoryDTO>(productCategoryDTO, HttpStatus.OK);
	}

	@PostMapping("/product_category")
	public ResponseEntity<?> create(@Valid @RequestBody ProductCategoryDTO productCategoryDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		
		ProductCategoryDTO nuevoProductCategoryDTO = this.productCategoryService.save(productCategoryDTO);
		
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El ProductCategory ha sido creado con éxito!");
		response.put("productCategory", nuevoProductCategoryDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/product_category/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductCategoryDTO productCategoryDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		
		ProductCategoryDTO updatedProductCategoryDTO = this.productCategoryService.update(id,productCategoryDTO);

		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El ProductCategory ha sido actualizado con éxito!");
		response.put("productCategory", updatedProductCategoryDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/product_category/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		this.productCategoryService.delete(id);
		
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El ProductCategory ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
