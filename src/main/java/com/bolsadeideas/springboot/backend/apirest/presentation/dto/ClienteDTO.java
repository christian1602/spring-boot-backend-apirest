package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteDTO(
		Long id,		
		@NotBlank @Size(min = 4, max = 12) String nombre,		
		@NotBlank String apellido,		
		@NotBlank @Email String email,
		Date createdAt) {
}
