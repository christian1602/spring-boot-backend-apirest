package com.bolsadeideas.springboot.backend.apirest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductCategoryNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ProductCategoryNotFoundException(String message) {
		super(message);
	}
}
