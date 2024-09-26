package com.bolsadeideas.springboot.backend.apirest.controllers;

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

import com.bolsadeideas.springboot.backend.apirest.models.entity.Category;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Product;
import com.bolsadeideas.springboot.backend.apirest.models.entity.ProductCategory;
import com.bolsadeideas.springboot.backend.apirest.models.services.ICategoryValidationService;
import com.bolsadeideas.springboot.backend.apirest.models.services.IProductCategoryService;
import com.bolsadeideas.springboot.backend.apirest.models.services.IProductValidationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" })
public class ProductCategoryRestController {

	private final IProductCategoryService productCategoryService;
	private final IProductValidationService productValidationService;
	private final ICategoryValidationService categoryValidationService;

	public ProductCategoryRestController(
			IProductCategoryService productCategoryService, 
			IProductValidationService productValidationService, 
			ICategoryValidationService categoryValidationService
	) {
		this.productCategoryService = productCategoryService;
		this.productValidationService = productValidationService;
		this.categoryValidationService = categoryValidationService;
	}
	
	@GetMapping("/product_category")
	public List<ProductCategory> index() {
		return this.productCategoryService.findAll();
	}

	@GetMapping("/product_category/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		ProductCategory productCategory = null;

		Map<String, Object> response = new HashMap<>();

		try {
			productCategory = this.productCategoryService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (productCategory == null) {
			response.put("mensaje",
					"El ProductCategory con el ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<ProductCategory>(productCategory, HttpStatus.OK);
	}

	@PostMapping("/product_category")
	public ResponseEntity<?> create(@Valid @RequestBody ProductCategory productCategory, BindingResult result) {
		ProductCategory nuevoProductCategory = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		ResponseEntity<?> productValidationResponse = this.productValidationService.validateProduct(productCategory, response);
		if (productValidationResponse != null) {
			return productValidationResponse;
		}
		
		ResponseEntity<?> categoryValidationResponse = this.categoryValidationService.validateCategory(productCategory, response);
		if (categoryValidationResponse != null) {
			return categoryValidationResponse;
		}

		try {
			nuevoProductCategory = this.productCategoryService.save(productCategory);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El ProductCategory ha sido creado con éxito!");
		response.put("productCategory", nuevoProductCategory);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/product_category/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody ProductCategory productCategory, BindingResult result,
			@PathVariable Long id) {
		ProductCategory productCategoryActual = this.productCategoryService.findById(id);
		ProductCategory productCategoryActualizado = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (productCategoryActual == null) {
			response.put("mensaje", "Error: No se pudo editar, el ProductCategory con el ID: ".concat(id.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		ResponseEntity<?> productValidationResponse = this.productValidationService.validateProduct(productCategory, response);
		if (productValidationResponse != null) {
			return productValidationResponse;
		}
		
		ResponseEntity<?> categoryValidationResponse = this.categoryValidationService.validateCategory(productCategory, response);
		if (categoryValidationResponse != null) {
			return categoryValidationResponse;
		}

		try {
			Product product = new Product();
			product.setId(productCategory.getProduct().getId());

			Category category = new Category();
			category.setId(productCategory.getCategory().getId());

			productCategoryActual.setDescription(productCategory.getDescription());
			productCategoryActual.setProduct(product);
			productCategoryActual.setCategory(category);

			productCategoryActualizado = this.productCategoryService.save(productCategoryActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el ProductCategory en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El ProductCategory ha sido actualizado con éxito!");
		response.put("productCategory", productCategoryActualizado);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/product_category/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		ProductCategory productCategory = this.productCategoryService.findById(id);
		if (productCategory == null) {
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
