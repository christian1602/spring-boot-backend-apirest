package com.bolsadeideas.springboot.backend.apirest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductAndCategoryAlreadyExistsInProductCategoryException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ProductAndCategoryAlreadyExistsInProductCategoryException(String message) {
		super(message);
	}
}
