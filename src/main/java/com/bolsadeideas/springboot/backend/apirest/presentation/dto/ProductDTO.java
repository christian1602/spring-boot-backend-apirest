package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductDTO(
		Long id,
		@NotBlank String name) {	
}
