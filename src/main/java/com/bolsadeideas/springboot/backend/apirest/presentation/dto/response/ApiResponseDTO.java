package com.bolsadeideas.springboot.backend.apirest.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"message","data"})
public record ApiResponseDTO<T>(
		String message, 
		T data) {
}
