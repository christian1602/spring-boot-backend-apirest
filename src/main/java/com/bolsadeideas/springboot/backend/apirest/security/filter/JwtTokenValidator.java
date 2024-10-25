package com.bolsadeideas.springboot.backend.apirest.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bolsadeideas.springboot.backend.apirest.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

	private static final String BEARER_PREFIX = "Bearer ";
	private final JwtUtils jwtUtils;

	public JwtTokenValidator(JwtUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		// EXTRAER EL TOKEN JWT DE LA CABECERA: Authorization
		String jwtToken = this.extractToken(request);
		
		if (jwtToken != null) {
			try {
				// INTENTAR OBTENER LA AUTENTICACION A PARTIR DEL TOKEN JWT
				Authentication authentication = this.getAuthentication(jwtToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (BadCredentialsException ex) {
				// MANEJO DE EXCEPCION PARA TOKEN INVALIDO
		        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: " + ex.getMessage());
		        return; // SALIR DEL FILTRO SI EL TOKEN NO ES VÁLIDO
	        } catch (TokenExpiredException ex) {
	            // MANEJO SSPECIFICO PARA TOKEN EXPIRADO
	            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
	            return; // SALIR DEL FILTRO SI EL TOKEN HA EXPIRADO
	        } catch (AuthenticationException ex) {
	            // MANEJO DE CUALQUIER OTRA EXCEPCION DE AUTENTICACION
	            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + ex.getMessage());
	            return; // SALIR DEL FILTRO EN CASO DE FALLA DE AUTENTICACIÓN
	        }
		}
		
		// CONTINUAR CON EL SIGUIENTE FILTRO
		// filterChain MANEJA LA SECUENCIA DE FILTROS
		filterChain.doFilter(request, response);
	}

	private Authentication getAuthentication(String jwtToken) throws AuthenticationException {
		
		try {
			DecodedJWT decodedJWT = this.jwtUtils.validateToken(jwtToken);
			
			String username = this.jwtUtils.extractUsername(decodedJWT);
			String stringAuthorities = this.jwtUtils.getEspecificClaim(decodedJWT, "authorities").asString();		
			
			Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);			
			
			return new UsernamePasswordAuthenticationToken(username, null, authorities);
		} catch (JWTVerificationException ex) {
	        throw new BadCredentialsException("Invalid token: " + ex.getMessage(), ex);
	    } catch (Exception ex) {
	        throw new AuthenticationServiceException("Authentication failed", ex);
	    }		
	}

	private String extractToken(HttpServletRequest request) {
		// EJEMPLO: Authorization: bearer VALOR_DE_MI_TOKEN
		return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
				.filter(header -> header.startsWith(BEARER_PREFIX))
				.map(header -> header.substring(BEARER_PREFIX.length())).orElse(null);
	}
}
