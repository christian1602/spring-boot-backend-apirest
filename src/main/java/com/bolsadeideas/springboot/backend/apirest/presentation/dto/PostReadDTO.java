package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostReadDTO(
		@NotNull Long id,
		@NotBlank String title,
		@NotBlank String body) {	
}
