package com.bolsadeideas.springboot.backend.apirest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryAlreadyExistsInProductCategoryException extends RuntimeException {
	
	@Serial // INDICA QUE ESTE CAMPO ES PARTE DEL MECANISMO DE SERIALIZACION
	private static final long serialVersionUID = 1L;

	public CategoryAlreadyExistsInProductCategoryException(String message) {
		super(message);
	}
}
