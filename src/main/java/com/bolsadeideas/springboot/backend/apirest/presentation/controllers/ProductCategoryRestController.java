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

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductCategoryDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.ICategoryService;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProductCategoryService;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" })
public class ProductCategoryRestController {

	private final IProductCategoryService productCategoryService;
	private final IProductService productService;
	private final ICategoryService categoryService;

	public ProductCategoryRestController(
			IProductCategoryService productCategoryService, 
			IProductService productService,
			ICategoryService categoryService) {
		this.productCategoryService = productCategoryService;
		this.productService = productService;
		this.categoryService = categoryService;
	}

	@GetMapping("/product_category")
	public List<ProductCategoryDTO> index() {
		return this.productCategoryService.findAll();
	}

	@GetMapping("/product_category/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		ProductCategoryDTO productCategoryDTO = null;

		Map<String, Object> response = new HashMap<>();

		try {
			productCategoryDTO = this.productCategoryService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (productCategoryDTO == null) {
			response.put("mensaje",
					"El ProductCategory con el ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<ProductCategoryDTO>(productCategoryDTO, HttpStatus.OK);
	}

	@PostMapping("/product_category")
	public ResponseEntity<?> create(@Valid @RequestBody ProductCategoryDTO productCategoryDTO, BindingResult result) {
		ProductCategoryDTO nuevoProductCategoryDTO = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		ProductDTO productDTOActual = null;
		Long productIdDTOActual = 0L;

		try {
			// RECUPEAR EL PRODUCT DESDE LA BASE DE DATOS
			productIdDTOActual = productCategoryDTO.getProductId();
			productDTOActual = this.productService.findById(productIdDTOActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (productDTOActual == null) {
			response.put("mensaje", "El Product con el ID: ".concat(productIdDTOActual.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		CategoryDTO categoryDTOActual = null;
		Long categoryIdDTOActual = 0L;

		try {
			// RECUPEAR EL CATEGORY DESDE LA BASE DE DATOS
			categoryIdDTOActual = productCategoryDTO.getCategoryId();
			categoryDTOActual = this.categoryService.findById(categoryIdDTOActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (categoryDTOActual == null) {
			response.put("mensaje", "El Category con el ID: ".concat(categoryIdDTOActual.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			nuevoProductCategoryDTO = this.productCategoryService.save(productCategoryDTO);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El ProductCategory ha sido creado con éxito!");
		response.put("productCategory", nuevoProductCategoryDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/product_category/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody ProductCategoryDTO productCategoryDTO, BindingResult result,
			@PathVariable Long id) {
		ProductCategoryDTO productCategoryDTOActual = this.productCategoryService.findById(id);
		ProductCategoryDTO productCategoryDTOActualizado = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (productCategoryDTOActual == null) {
			response.put("mensaje", "Error: No se pudo editar, el ProductCategory con el ID: ".concat(id.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		ProductDTO productDTOActual = null;
		Long productIdDTOActual = 0L;

		try {
			// RECUPEAR EL PRODUCT DESDE LA BASE DE DATOS
			productIdDTOActual = productCategoryDTO.getProductId();
			productDTOActual = this.productService.findById(productIdDTOActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (productDTOActual == null) {
			response.put("mensaje", "El Product con el ID: ".concat(productIdDTOActual.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		CategoryDTO categoryDTOActual = null;
		Long categoryIdDTOActual = 0L;

		try {
			// RECUPEAR EL CATEGORY DESDE LA BASE DE DATOS
			categoryIdDTOActual = productCategoryDTO.getCategoryId();
			categoryDTOActual = this.categoryService.findById(categoryIdDTOActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (categoryDTOActual == null) {
			response.put("mensaje", "El Category con el ID: ".concat(categoryIdDTOActual.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			productCategoryDTOActual.setDescription(productCategoryDTO.getDescription());
			productCategoryDTOActual.setProductId(productCategoryDTO.getProductId());
			productCategoryDTOActual.setCategoryId(productCategoryDTO.getCategoryId());
			productCategoryDTOActualizado = this.productCategoryService.save(productCategoryDTOActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el ProductCategory en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El ProductCategory ha sido actualizado con éxito!");
		response.put("productCategory", productCategoryDTOActualizado);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/product_category/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		ProductCategoryDTO productCategoryDTO = this.productCategoryService.findById(id);
		if (productCategoryDTO == null) {
			response.put("mensaje", "Error: no se pudo eliminar, el ProductCategory con ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			this.productCategoryService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el ProductCategory de la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El ProductCategory ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
