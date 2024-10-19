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
		ProductDTO productDTO = null;

		Map<String, Object> response = new HashMap<>();

		try {
			productDTO = this.productService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (productDTO == null) {
			response.put("mensaje",
					"El Product con el ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
	}

	@PostMapping("/products")
	public ResponseEntity<?> create(@Valid @RequestBody ProductDTO productDTO, BindingResult result) {		
		ProductDTO nuevoProductoDTO = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {			
			nuevoProductoDTO = this.productService.save(productDTO);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El producto ha sido creado con éxito!");
		response.put("producto", nuevoProductoDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody ProductDTO productDTO, BindingResult result, @PathVariable Long id) {
		ProductDTO productoActualDTO = this.productService.findById(id);
		ProductDTO productActualizadoDTO = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (productoActualDTO == null) {
			response.put("mensaje", "Error: No se pudo editar, el Product con el ID: ".concat(id.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			productoActualDTO.setName(productDTO.getName());
			productActualizadoDTO = this.productService.save(productoActualDTO);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el cliente en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Product ha sido actualizado con éxito!");
		response.put("producto", productActualizadoDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		ProductDTO productoDTO = this.productService.findById(id);
		
		if (productoDTO == null) {
			response.put("mensaje", "Error: no se pudo eliminar, el Product con ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		// TODO: ELIMINAR TODOS LOS PRODUCTOS ASOCIADOS EN PRODUCTCATEGORY
		try {
			this.productService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el Product de la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Product ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
