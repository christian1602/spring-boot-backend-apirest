package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryWriteDTO(
		@NotBlank String name) {
}
