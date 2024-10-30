package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCategoryReadDTO(
		@NotNull Long id,
		@NotBlank String description) {
}
