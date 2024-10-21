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
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" })
public class ProductRestController {

	private final IProductService productService;

	public ProductRestController(IProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/products")
	public List<ProductDTO> index() {
		return this.productService.findAll();
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		ProductDTO productDTO = this.productService.findById(id);
		return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
	}

	@PostMapping("/products")
	public ResponseEntity<?> create(@Valid @RequestBody ProductDTO productDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		
		ProductDTO nuevoProductoDTO = this.productService.save(productDTO);		

		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El producto ha sido creado con éxito!");
		response.put("producto", nuevoProductoDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		
		ProductDTO updatedProductDTO = this.productService.update(id,productDTO);		

		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El Product ha sido actualizado con éxito!");
		response.put("producto", updatedProductDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		this.productService.delete(id);
		
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El Product ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
