package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
		@NotNull Long id,
		@NotBlank @Email String email,
		@NotBlank String username,
		@NotBlank String password,
		boolean enabled,
		boolean accountNoExpired,
		boolean accountNoLocked,
		boolean credentialNoExpired,
		LocalDateTime lastPasswordChange,
		@Valid AuthRolesDTO authRolesDTO) {
}
