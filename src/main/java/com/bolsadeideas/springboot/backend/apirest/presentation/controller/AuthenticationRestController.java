package com.bolsadeideas.springboot.backend.apirest.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.exception.InvalidDataException;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthLoginDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UserWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.response.AuthResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.RefreshTokenDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UpdatePasswordDTO;
import com.bolsadeideas.springboot.backend.apirest.service.implementation.UserDetailsServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200"})
@Tag(name = "Authentication", description = "Controller for Authentication")
public class AuthenticationRestController {
	
	private final UserDetailsServiceImpl userDetailsService;

    public AuthenticationRestController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    @PostMapping("/sign-up")
    public ResponseEntity<?> register(@Valid @RequestBody UserWriteDTO userWriteDTO, BindingResult result){
    	if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
    	
    	AuthResponseDTO authResponseDTO = this.userDetailsService.createUser(userWriteDTO);
    	return new ResponseEntity<>(authResponseDTO, HttpStatus.CREATED);
    }
    
    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UserWriteDTO userWriteDTO, BindingResult result){
    	if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
    	
    	AuthResponseDTO authResponseDTO = this.userDetailsService.updateUser(id, userWriteDTO);
    	return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
    }
    
    @PostMapping("/log-in")
    @Operation(
    		summary = "Login user", 
    		description = "Authenticate a user and return the authentication token along with user details",
    		tags = {"Authentication"},
    		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
    				description = "Authentication request with username and password",
    				required = true,
    				content = @Content(
    						mediaType = "application/json",
    						schema = @Schema(implementation = AuthLoginDTO.class)
    						)
    				),
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "Successful authentication",
    						content = @Content(
    								mediaType = "application/json",
    								schema = @Schema(implementation = AuthResponseDTO.class)
    								)
    						)
    				}
    		)
    public ResponseEntity<?> login(@Valid @RequestBody AuthLoginDTO authLoginDTO, BindingResult result){
    	if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
    	
    	AuthResponseDTO authResponseDTO = this.userDetailsService.loginUser(authLoginDTO);
    	return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO, BindingResult result){
    	if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
    	
    	AuthResponseDTO authResponseDTO = this.userDetailsService.refreshToken(refreshTokenDTO);
    	return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
    }
    
    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO, BindingResult result){
    	if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
    	
    	AuthResponseDTO authResponseDTO = this.userDetailsService.updatePassword(updatePasswordDTO);    	
    	return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
    }
}
