package com.bolsadeideas.springboot.backend.apirest.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthLoginDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CreateUserDTO;
import com.bolsadeideas.springboot.backend.apirest.service.implementation.UserDetailsServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = { "http://localhost:4200 " })
@Tag(name = "Authentication", description = "Controller for Authentication")
public class AuthenticationRestController {
	
	private final UserDetailsServiceImpl userDetailsService;

    public AuthenticationRestController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody CreateUserDTO createUserDTO){
        try {
            AuthResponseDTO response = this.userDetailsService.createUser(createUserDTO);
            return new ResponseEntity<AuthResponseDTO>(response, HttpStatus.CREATED);
        } catch(IllegalArgumentException ex){
            // Esta excepción se lanza si los roles no existen
            return new ResponseEntity<AuthResponseDTO>(new AuthResponseDTO(createUserDTO.username(), ex.getMessage(), null, false), HttpStatus.BAD_REQUEST);
        } catch(Exception ex) {
            // Manejo de errores genéricos
            return new ResponseEntity<AuthResponseDTO>(new AuthResponseDTO(createUserDTO.username(), "Error during registration", null, false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthLoginDTO authLoginDTO){
        try {
            return new ResponseEntity<AuthResponseDTO>(this.userDetailsService.loginUser(authLoginDTO), HttpStatus.OK);
        } catch(BadCredentialsException ex){
            // Manejo de errores: devolver 401 Unauthorized
            return new ResponseEntity<AuthResponseDTO>(new AuthResponseDTO(authLoginDTO.username(), ex.getMessage(), null, false), HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            // Manejo de errores genéricos
            return new ResponseEntity<AuthResponseDTO>(new AuthResponseDTO(authLoginDTO.username(), "Error during login", null, false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
