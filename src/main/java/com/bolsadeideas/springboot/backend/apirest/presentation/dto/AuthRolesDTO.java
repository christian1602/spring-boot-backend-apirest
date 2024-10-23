package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import java.util.List;

import jakarta.validation.constraints.Size;

public record AuthRolesDTO(
		 @Size(max = 3, message = "The user cannot have more than 3 roles") List<String> roleListName) {
}
