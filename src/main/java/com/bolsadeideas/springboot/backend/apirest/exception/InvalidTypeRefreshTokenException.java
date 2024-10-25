package com.bolsadeideas.springboot.backend.apirest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTypeRefreshTokenException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InvalidTypeRefreshTokenException(String message) {
		super(message);
	}
}
