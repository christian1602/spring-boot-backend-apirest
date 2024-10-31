package com.bolsadeideas.springboot.backend.apirest.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username","message","access_token","refresh_token","status"})
public record AuthResponseDTO(
		String username,
	    String message,
	    @JsonProperty("access_token")
	    String accessToken,
	    @JsonProperty("refresh_token")
	    String refreshToken,
	    boolean status) {
}
