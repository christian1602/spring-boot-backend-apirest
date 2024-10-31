package com.bolsadeideas.springboot.backend.apirest.presentation.controller;

import java.util.List;

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

import com.bolsadeideas.springboot.backend.apirest.exception.InvalidDataException;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.response.ApiResponseDTO;
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
	public List<ProductReadDTO> index() {
		return this.productService.findAll();
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		ProductReadDTO productReadDTO = this.productService.findById(id);
		return new ResponseEntity<>(productReadDTO, HttpStatus.OK);
	}

	@PostMapping("/products")
	public ResponseEntity<?> create(@Valid @RequestBody ProductWriteDTO productWriteDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		
		ProductReadDTO newProductoReadDTO = this.productService.save(productWriteDTO);		
		ApiResponseDTO<ProductReadDTO> response = new ApiResponseDTO<>("¡El producto ha sido creado con éxito!",newProductoReadDTO);		

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductWriteDTO productWriteDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		
		ProductReadDTO updatedProductReadDTO = this.productService.update(id,productWriteDTO);
		ApiResponseDTO<ProductReadDTO> response = new ApiResponseDTO<>("¡El Product ha sido actualizado con éxito!",updatedProductReadDTO);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		this.productService.delete(id);
		
		ApiResponseDTO<ProductReadDTO> response = new ApiResponseDTO<>("¡El Product ha sido eliminado con éxito!",null);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
