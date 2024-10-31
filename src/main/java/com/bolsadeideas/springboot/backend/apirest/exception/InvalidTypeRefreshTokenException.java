package com.bolsadeideas.springboot.backend.apirest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTypeRefreshTokenException extends RuntimeException {
	
	@Serial
	private static final long serialVersionUID = 1L;

	public InvalidTypeRefreshTokenException(String message) {
		super(message);
	}
}
