package com.bolsadeideas.springboot.backend.apirest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
 
@ResponseStatus(HttpStatus.CONFLICT)
public class ProductAlreadyExistsInProductCategoryException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ProductAlreadyExistsInProductCategoryException(String message) {
		super(message);
	}
}
