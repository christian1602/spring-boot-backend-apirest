package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCategoryWriteDTO(		
		@NotBlank String description,
		@NotNull Long productId,
		@NotNull Long categoryId) {
}
