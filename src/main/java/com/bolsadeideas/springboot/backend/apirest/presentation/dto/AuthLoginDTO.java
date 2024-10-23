package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginDTO(
		@NotBlank String username,
        @NotBlank String password) {
}
