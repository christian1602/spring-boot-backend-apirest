package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username","message","jwt","status"})
public record AuthResponseDTO(
		String username,
	    String message,
	    String jwt,
	    boolean status) {
}
