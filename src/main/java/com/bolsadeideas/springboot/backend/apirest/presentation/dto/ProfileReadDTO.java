package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfileReadDTO(
		@NotNull Long id,
		@NotBlank String bio,
		@NotBlank String website) {
}
