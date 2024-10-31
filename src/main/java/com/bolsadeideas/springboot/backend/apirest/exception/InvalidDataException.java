package com.bolsadeideas.springboot.backend.apirest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDataException extends RuntimeException{

	@Serial
	private static final long serialVersionUID = 1L;
	
	private final BindingResult result;
	
	public InvalidDataException(BindingResult result) {
		this.result = result;
	}
	
	public BindingResult getResult() {
		return this.result;
	}
}