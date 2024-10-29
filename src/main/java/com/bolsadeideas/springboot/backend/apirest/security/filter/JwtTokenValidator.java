package com.bolsadeideas.springboot.backend.apirest.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

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
		// OBTENER EL URI DE LA SOLICITUD
		String requestURI = request.getRequestURI();
		HttpMethod requestMethod = HttpMethod.valueOf(request.getMethod());
		
		// VERIFICAR SI EL ENDPOINT NO REQUIERE AUTENTICACION
		Set<String> publicEndpoints = Set.of("/auth/sign-up", "/auth/log-in", "/auth/refresh-token");
		if ((requestMethod == HttpMethod.POST) && publicEndpoints.contains(requestURI)) { // PERMTIIR ACCESO SI ES UNO DE LOS ENDPOINTS PERMITIDOS
			System.out.println("ESTAMOS EN UN ENDPOINT QUE NO REQUIERE JWT");
			filterChain.doFilter(request,response);
			return; // SALIR DEL FILTRO
		}
		
		System.out.println("ENTRANDO AL CODIGO QUE VALIDA SOLO ENDPOINTS QUE SI REQUIREN JWT");
		
		// EXTRAER EL TOKEN JWT DE LA CABECERA: Authorization
		String accessToken = this.extractToken(request);

		System.out.println("accessToken => " + accessToken);
		// SI EL TOKEN ES NULO EN UN ENDPOINT QUE REQUIERE AUTENTICACION, ENVIAR ERROR 401
		if (accessToken == null) {
			System.out.println("EL accessToken ES NULL");
			filterChain.doFilter(request, response);
			// response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token is missing");
			return; // DETENER EL FLUJO
		}		
		
		// INTENTAR OBTENER LA AUTENTICACION A PARTIR DEL TOKEN JWT		
		Authentication authentication = this.getAuthentication(accessToken);
		    
		if (authentication == null) {
			System.out.println("LA authentication ES NULL");
			filterChain.doFilter(request, response);
		    // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token, not Authorized at doFilterInternal");
		    return; // SALIR DEL FILTRO SI EL TOKEN NO ES VALIDO
		}

		System.out.println("TODO SALIO BIEN Y ESTABLECEMOS EL SecurityContextHolder");
		System.out.println("Permisos del usuario: " + authentication.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// CONTINUAR CON EL SIGUIENTE FILTRO
		filterChain.doFilter(request, response);		
		
		/*
		if (accessToken != null){
			// INTENTAR OBTENER LA AUTENTICACION A PARTIR DEL TOKEN JWT
			Authentication authentication = this.getAuthentication(accessToken);
			
			if (authentication == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid token, not Authorized at doFilterInternal");
				return; // DETENER EL FLUJO
			}

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		// CONTINUAR CON EL SIGUIENTE FILTRO
		// filterChain MANEJA LA SECUENCIA DE FILTROS
		filterChain.doFilter(request,response);
		*/
	}

	private Authentication getAuthentication(String accessToken) {	
		// VALIDAR EL TOKEN Y CAPTURAR EXCEPCIONES
		DecodedJWT decodedJWT;

		try {
			decodedJWT = this.jwtUtils.validateToken(accessToken);
		} catch(Exception e){
			return null;
		}

		// GENERAMOS Y RETORNAMOS EL Authentication
		String username = this.jwtUtils.extractUsername(decodedJWT);
		String stringAuthorities = this.jwtUtils.getEspecificClaim(decodedJWT,"authorities").asString();
		System.out.println("Autoridades extraídas: " + stringAuthorities);
		Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

		return new UsernamePasswordAuthenticationToken(username,null,authorities);
	}

	private String extractToken(HttpServletRequest request) {
		// EJEMPLO: Authorization: bearer VALOR_DE_MI_TOKEN
		return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
				.filter(header -> header.startsWith(BEARER_PREFIX))
				.map(header -> header.substring(BEARER_PREFIX.length())).orElse(null);
	}
}
