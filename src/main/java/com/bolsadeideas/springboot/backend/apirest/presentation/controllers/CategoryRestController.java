package com.bolsadeideas.springboot.backend.apirest.presentation.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
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

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.CategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryDTO;
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
	public List<CategoryDTO> index() {
		return this.categoryService.findAll();
	}

	@GetMapping("/categories/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		CategoryDTO categoriaDTO = null;

		Map<String, Object> response = new HashMap<>();

		try {
			categoriaDTO = this.categoryService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (categoriaDTO == null) {
			response.put("mensaje",
					"El cliente con el ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<CategoryDTO>(categoriaDTO, HttpStatus.OK);
	}

	@PostMapping("/categories")
	public ResponseEntity<?> create(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
		CategoryDTO nuevaCategoriaDTO = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			nuevaCategoriaDTO = this.categoryService.save(categoryDTO);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡La categoria ha sido creada con éxito!");
		response.put("categoria", nuevaCategoriaDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/categories/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result,
			@PathVariable Long id) {
		CategoryDTO categoriaActualDTO = this.categoryService.findById(id);
		CategoryDTO categoriaActualizadaDTO = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (categoriaActualDTO == null) {
			response.put("mensaje", "Error: No se pudo editar, la Categoria con el ID: ".concat(id.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			categoriaActualDTO.setName(categoryDTO.getName());
			categoriaActualizadaDTO = this.categoryService.save(categoriaActualDTO);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la categoria en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡La Categoria ha sido actualizada con éxito!");
		response.put("categoria", categoriaActualizadaDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/categories/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		CategoryDTO categoriaDTO = this.categoryService.findById(id);
		if (categoriaDTO == null) {
			response.put("mensaje", "Error: no se pudo eliminar, el Category con ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		// TODO: ELIMINAR TODOS LOS PRODUCTOS ASOCIADOS EN PRODUCTCATEGORY
		
		try {
			this.categoryService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el Category de la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Category ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
