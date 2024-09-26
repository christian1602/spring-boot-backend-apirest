package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.springboot.backend.apirest.models.entity.ProductCategory;

public interface ICategoryValidationService {
	ResponseEntity<?> validateCategory(ProductCategory productCategory, Map<String,Object>response);
}
