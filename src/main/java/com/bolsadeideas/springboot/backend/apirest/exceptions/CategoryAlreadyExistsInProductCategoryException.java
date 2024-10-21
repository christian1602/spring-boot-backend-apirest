package com.bolsadeideas.springboot.backend.apirest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryAlreadyExistsInProductCategoryException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public CategoryAlreadyExistsInProductCategoryException(String message) {
		super(message);
	}
}
