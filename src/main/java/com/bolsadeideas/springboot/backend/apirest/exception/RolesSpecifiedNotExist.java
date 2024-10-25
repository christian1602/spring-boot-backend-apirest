package com.bolsadeideas.springboot.backend.apirest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RolesSpecifiedNotExist extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public RolesSpecifiedNotExist(String message) {
		super(message);
	}
}
