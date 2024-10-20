package com.bolsadeideas.springboot.backend.apirest.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handleUserNotFoundException(UserNotFoundException ex){
		Map<String, Object> response = new HashMap<>();		
		response.put("mensaje", "User not found");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PostNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handlePostNotFoundException(PostNotFoundException ex){
		Map<String, Object> response = new HashMap<>();		
		response.put("mensaje", "Post not found");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ProfileNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handleProfileNotFoundException(ProfileNotFoundException ex){
		Map<String, Object> response = new HashMap<>();		
		response.put("mensaje", "Profile not found");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(UserNotCreatorException.class)
	public ResponseEntity<Map<String,Object>> handleUserNotCreatorException(UserNotCreatorException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "User is not the creator");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(UserAlreadyHasProfileException.class)
	public ResponseEntity<Map<String,Object>> handleUserAlreadyHasProfileException(UserAlreadyHasProfileException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "User already has a profile");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<Map<String,Object>> handleInvalidDataException(InvalidDataException ex){
		Map<String, Object> response = new HashMap<>();
		List<String> errors = ex.getResult().getFieldErrors().stream()
	            .map(fieldError -> "El campo '"
	            .concat(fieldError.getField())
	            .concat("' ")
	            .concat(fieldError.getDefaultMessage()))
	            .collect(Collectors.toList());

        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}	
	
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<Map<String,Object>> handleDataAccessException(DataAccessException ex){
		Map<String, Object> response = new HashMap<>();		
		response.put("mensaje", "Database operation error");
		response.put("error", ex.getMessage().concat(" : ").concat(ex.getMostSpecificCause().getMessage()));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(DataIntegrityViolationException .class)
	public ResponseEntity<Map<String,Object>> handleDataIntegrityViolationException (DataIntegrityViolationException  ex){
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "Data integrity violation");
		response.put("error", ex.getMessage().concat(" : ").concat(ex.getMostSpecificCause().getMessage()));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
