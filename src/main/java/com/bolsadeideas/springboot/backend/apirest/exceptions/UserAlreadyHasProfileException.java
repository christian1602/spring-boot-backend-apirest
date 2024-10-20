package com.bolsadeideas.springboot.backend.apirest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyHasProfileException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UserAlreadyHasProfileException(String message) {
		super(message);
	}
}
