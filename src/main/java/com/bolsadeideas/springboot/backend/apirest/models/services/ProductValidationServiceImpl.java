package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Product;
import com.bolsadeideas.springboot.backend.apirest.models.entity.ProductCategory;

@Service
public class ProductValidationServiceImpl implements IProductValidationService {

	private IProductService productService;

	public ProductValidationServiceImpl(IProductService productService) {
		this.productService = productService;
	}

	@Override
	public ResponseEntity<?> validateProduct(ProductCategory productCategory, Map<String, Object> response) {
		Product productActual = null;
		Long idProductActual = 0L;

		try {
			idProductActual = productCategory.getProduct().getId();
			productActual = this.productService.findById(idProductActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (productActual == null) {
			response.put("mensaje", "El Product con el ID: ".concat(idProductActual.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return null;
	}
}
