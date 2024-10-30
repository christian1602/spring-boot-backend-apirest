package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record PostReadDTO(
		Long id,
		@NotBlank String title,
		@NotBlank String body) {	
}
