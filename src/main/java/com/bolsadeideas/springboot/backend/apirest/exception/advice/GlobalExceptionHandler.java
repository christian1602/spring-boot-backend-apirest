package com.bolsadeideas.springboot.backend.apirest.exception.advice;

import java.util.List;
import java.util.Optional;
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
	public ResponseEntity<ErrorResponse> handleClienteNotFoundException(ClienteNotFoundException ex){
		ErrorResponse response = new ErrorResponse("Cliente not found",ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex){
		ErrorResponse response = new ErrorResponse("User not found",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex){
		ErrorResponse response = new ErrorResponse("Username not found",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(RolesSpecifiedNotExist.class)
	public ResponseEntity<ErrorResponse> handleRolesSpecifiedNotExist(RolesSpecifiedNotExist ex){
		ErrorResponse response = new ErrorResponse("Roles specified does not exist",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);		
	}	
	
	@ExceptionHandler(PostNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException ex){
		ErrorResponse response = new ErrorResponse("Post not found",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ProfileNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProfileNotFoundException(ProfileNotFoundException ex){
		ErrorResponse response = new ErrorResponse("Profile not found",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserNotCreatorException.class)
	public ResponseEntity<ErrorResponse> handleUserNotCreatorException(UserNotCreatorException ex){
		ErrorResponse response = new ErrorResponse("User is not the creator",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex){
		ErrorResponse response = new ErrorResponse("Product not found",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex){
		ErrorResponse response = new ErrorResponse("Category not found",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ProductCategoryNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProductCategoryNotFoundException(ProductCategoryNotFoundException ex){
		ErrorResponse response = new ErrorResponse("Product and Category not found",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UserAlreadyHasProfileException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyHasProfileException(UserAlreadyHasProfileException ex){
		ErrorResponse response = new ErrorResponse("User already has a profile",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ProductAlreadyExistsInProductCategoryException.class)
	public ResponseEntity<ErrorResponse> handleProductAlreadyExistsInProductCategoryException(ProductAlreadyExistsInProductCategoryException ex){
		ErrorResponse response = new ErrorResponse("Product already has categories",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(CategoryAlreadyExistsInProductCategoryException.class)
	public ResponseEntity<ErrorResponse> handleCategoryAlreadyExistsInProductCategoryException(CategoryAlreadyExistsInProductCategoryException ex){
		ErrorResponse response = new ErrorResponse("Category already has products",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InvalidRefreshTokenException.class)
	public ResponseEntity<ErrorResponse> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex){
		ErrorResponse response = new ErrorResponse("Invalid refresh token",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<ErrorResponse> handleInvalidDataException(InvalidDataException ex){		
		List<String> errors = ex.getResult().getFieldErrors().stream()
	            .map(fieldError -> "El campo '"
	            .concat(fieldError.getField())
	            .concat("' ")
				.concat(Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Sin mensaje de error")))
	            .collect(Collectors.toList());

		ErrorResponse response = new ErrorResponse("Invalid Data",errors);	    
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
		ErrorResponse response = new ErrorResponse("Invalid username or password",ex.getMessage());		
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);        
    }
	
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex){
		ErrorResponse response = new ErrorResponse("Database operation error",ex.getMessage().concat(" : ").concat(ex.getMostSpecificCause().getMessage()));		
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(DataIntegrityViolationException .class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException (DataIntegrityViolationException  ex){
		ErrorResponse response = new ErrorResponse("Data integrity violation",ex.getMessage().concat(" : ").concat(ex.getMostSpecificCause().getMessage()));		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
