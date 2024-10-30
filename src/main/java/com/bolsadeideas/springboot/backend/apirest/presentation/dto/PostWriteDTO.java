package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostWriteDTO(
		@NotBlank String title,
		@NotBlank String body,
		@NotNull Long userId) {
}
