/*
package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Category;
import com.bolsadeideas.springboot.backend.apirest.models.entity.ProductCategory;

@Service
public class CategoryValidationServiceImpl implements ICategoryValidationService {

	private final ICategoryService categoryService;

	public CategoryValidationServiceImpl(ICategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Override
	public ResponseEntity<?> validateCategory(ProductCategory productCategory, Map<String, Object> response) {
		Category categoryActual = null;
		Long idCategoryActual = 0L;

		try {
			idCategoryActual = productCategory.getCategory().getId();
			categoryActual = this.categoryService.findById(idCategoryActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (categoryActual == null) {
			response.put("mensaje", "El Category con el ID: ".concat(idCategoryActual.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return null;
	}
}
*/