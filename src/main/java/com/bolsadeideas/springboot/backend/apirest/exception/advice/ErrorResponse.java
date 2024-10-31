package com.bolsadeideas.springboot.backend.apirest.exception.advice;

public record ErrorResponse(
		String message, 
		Object error) {
}
