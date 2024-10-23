package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(
		@NotBlank @Email String email,
		@NotBlank String username,
		@NotBlank String password,
		@Valid AuthRolesDTO authRolesDTO) {
}
