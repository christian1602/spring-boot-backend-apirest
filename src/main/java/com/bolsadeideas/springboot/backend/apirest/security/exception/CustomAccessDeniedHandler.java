package com.bolsadeideas.springboot.backend.apirest.security.exception;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// ENVIAR UN ERROR 403 CUANDO EL ACCESO ES DENEGADO				
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: You do not have the necessary permissions");
	}
}
