package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordDTO(
		@NotBlank String username,
		@NotBlank String currentPassword,
		@NotBlank String newPassword) {
}
