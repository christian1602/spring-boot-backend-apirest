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
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.response.ApiResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.ICategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" })
public class CategoryRestController {

	private final ICategoryService categoryService;

	public CategoryRestController(ICategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping("/categories")
	public List<CategoryReadDTO> index() {
		return this.categoryService.findAll();
	}

	@GetMapping("/categories/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		CategoryReadDTO categoriaReadDTO = this.categoryService.findById(id);
		return new ResponseEntity<CategoryReadDTO>(categoriaReadDTO, HttpStatus.OK);
	}

	@PostMapping("/categories")
	public ResponseEntity<?> create(@Valid @RequestBody CategoryWriteDTO categoryWriteDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		
		CategoryReadDTO newCategoriaReadDTO = this.categoryService.save(categoryWriteDTO);
		ApiResponseDTO<CategoryReadDTO> response = new ApiResponseDTO<>("¡La categoria ha sido creada con éxito!",newCategoriaReadDTO);		

		return new ResponseEntity<ApiResponseDTO<CategoryReadDTO>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/categories/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CategoryWriteDTO categoryWriteDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
				
		CategoryReadDTO updatedCategoriaReadDTO = this.categoryService.update(id,categoryWriteDTO);
		ApiResponseDTO<CategoryReadDTO> response = new ApiResponseDTO<>("¡La Categoria ha sido actualizada con éxito!",updatedCategoriaReadDTO);		

		return new ResponseEntity<ApiResponseDTO<CategoryReadDTO>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/categories/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		this.categoryService.delete(id);
		
		ApiResponseDTO<CategoryReadDTO> response = new ApiResponseDTO<>("¡El Category ha sido eliminado con éxito!",null);		

		return new ResponseEntity<ApiResponseDTO<CategoryReadDTO>>(response, HttpStatus.OK);
	}
}
