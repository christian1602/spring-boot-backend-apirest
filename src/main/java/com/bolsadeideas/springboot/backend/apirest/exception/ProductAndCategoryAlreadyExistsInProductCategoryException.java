package com.bolsadeideas.springboot.backend.apirest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductAndCategoryAlreadyExistsInProductCategoryException extends RuntimeException {
	
	@Serial
	private static final long serialVersionUID = 1L;

	public ProductAndCategoryAlreadyExistsInProductCategoryException(String message) {
		super(message);
	}
}
