package com.bolsadeideas.springboot.backend.apirest.exception.advice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import com.bolsadeideas.springboot.backend.apirest.exception.CategoryAlreadyExistsInProductCategoryException;
import com.bolsadeideas.springboot.backend.apirest.exception.CategoryNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.exception.ClienteNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.exception.InvalidDataException;
import com.bolsadeideas.springboot.backend.apirest.exception.InvalidRefreshTokenException;
import com.bolsadeideas.springboot.backend.apirest.exception.PostNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.exception.ProductAlreadyExistsInProductCategoryException;
import com.bolsadeideas.springboot.backend.apirest.exception.ProductCategoryNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.exception.ProductNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.exception.ProfileNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.exception.RolesSpecifiedNotExist;
import com.bolsadeideas.springboot.backend.apirest.exception.UserAlreadyHasProfileException;
import com.bolsadeideas.springboot.backend.apirest.exception.UserNotCreatorException;
import com.bolsadeideas.springboot.backend.apirest.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ClienteNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handleClienteNotFoundException(ClienteNotFoundException ex){
		Map<String, Object> response = new HashMap<>();		
		response.put("mensaje", "Cliente not found");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handleUserNotFoundException(UserNotFoundException ex){
		Map<String, Object> response = new HashMap<>();		
		response.put("mensaje", "User not found");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handleUsernameNotFoundException(UsernameNotFoundException ex){
		Map<String, Object> response = new HashMap<>();		
		response.put("mensaje", "Username not found");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(RolesSpecifiedNotExist.class)
	public ResponseEntity<Map<String,Object>> handleRolesSpecifiedNotExist(RolesSpecifiedNotExist ex){
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "Roles specified does not exist");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);		
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
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handleProductNotFoundException(ProductNotFoundException ex){
		Map<String, Object> response = new HashMap<>();		
		response.put("mensaje", "Product not found");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handleCategoryNotFoundException(CategoryNotFoundException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "Category not found");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ProductCategoryNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handleProductCategoryNotFoundException(ProductCategoryNotFoundException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "Product and Category not found");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UserAlreadyHasProfileException.class)
	public ResponseEntity<Map<String,Object>> handleUserAlreadyHasProfileException(UserAlreadyHasProfileException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "User already has a profile");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ProductAlreadyExistsInProductCategoryException.class)
	public ResponseEntity<Map<String,Object>> handleProductAlreadyExistsInProductCategoryException(ProductAlreadyExistsInProductCategoryException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "Product already has categories");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(CategoryAlreadyExistsInProductCategoryException.class)
	public ResponseEntity<Map<String,Object>> handleCategoryAlreadyExistsInProductCategoryException(CategoryAlreadyExistsInProductCategoryException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "Category already has products");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InvalidRefreshTokenException.class)
	public ResponseEntity<Map<String,Object>> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex){
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "Invalid refresh token");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
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
	
	@ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "Invalid username or password");
		response.put("error", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);        
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
