package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfileWriteDTO(
		@NotBlank String bio,
		@NotBlank String website,
		@NotNull Long userId) {
}
