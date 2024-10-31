package com.bolsadeideas.springboot.backend.apirest.security.exception;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint  implements AuthenticationEntryPoint {

	@Override
	public void commence(
			HttpServletRequest request, 
			HttpServletResponse response,
			AuthenticationException authException
	) throws IOException {
		// ENVIAR UN ERROR 401 CUANDO NO SE ESTA AUTENTICADO
		System.out.println("USUARIO NO AUTENTICADO: " + request.getRequestURI());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Authentication token required");			
	}
}