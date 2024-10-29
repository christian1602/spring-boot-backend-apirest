package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserReadDTO(
		@NotBlank Long id,
		@NotBlank @Email String email,
		@NotBlank String username,
		@Valid AuthRolesDTO authRolesDTO) {
}
