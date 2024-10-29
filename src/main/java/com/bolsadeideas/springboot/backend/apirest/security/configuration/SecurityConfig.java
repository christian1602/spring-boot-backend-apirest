package com.bolsadeideas.springboot.backend.apirest.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.bolsadeideas.springboot.backend.apirest.security.exception.JwtAccessDeniedHandler;
import com.bolsadeideas.springboot.backend.apirest.security.exception.JwtAuthenticationEntryPoint;
import com.bolsadeideas.springboot.backend.apirest.security.filter.JwtTokenValidator;
import com.bolsadeideas.springboot.backend.apirest.service.implementation.UserDetailsServiceImpl;
import com.bolsadeideas.springboot.backend.apirest.utils.JwtUtils;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtUtils jwtUtils;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	public SecurityConfig(JwtUtils jwtUtils, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
		this.jwtUtils = jwtUtils;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
	}

	// PASO 1: CONFIGURANDO SECURITY FILTER CHAIN SIN ANOTACIONES EN EL CONTROLADOR 
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				// .formLogin(form -> form.disable())  // DESHABILITAR EL FORMULARIO DE INICIO DE SESION
				// .httpBasic(basic -> basic.disable())  // DESHABILITAR LA AUTENTICACION BASCICA
				// .logout(logout -> logout.disable())  // DESHABILITAR EL MANEJO DE LOGOUT
				// .anonymous(anonymous -> anonymous.disable())  // DESHABILITAR EL ACCESO ANONIMO
				// .httpBasic(Customizer.withDefaults()) NO USAMOS AUTENTICACION BASICA
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auths -> {
					// PERMITIR ACCESO PUBLICO A SWAGGER
		            auths.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();
					// ENDPOINTS PUBLICOS DE AUTENTICACION
					auths.requestMatchers(HttpMethod.POST, "/auth/sign-up").permitAll();
					auths.requestMatchers(HttpMethod.POST, "/auth/log-in").permitAll();
					auths.requestMatchers(HttpMethod.POST, "/auth/refresh-token").permitAll();
					
					// auths.requestMatchers(HttpMethod.GET, "/api/posts-webclient").permitAll();

					// ENDPOINTS PRIVADOS
					// EJEMPLO DE DAR UN ROLE_USER QUE SOLO TIENE PERMISO DE READ
					// Y EN EL CONTROLADOR SOLO ENTRARA AL PERMISO READ MAS NO AL CREATE
					// auths.requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("ADMIN","DEVELOPER");
					// auths.requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("ADMIN","DEVELOPER");
					
					// Configurar acceso a usuarios con ROLE_USER
	                auths.requestMatchers(HttpMethod.GET, "/api/users").hasAuthority("READ"); // Permite acceso solo a READ
	                auths.requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAuthority("CREATE"); // Permite acceso solo a CREATE
	                auths.requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("DELETE"); // Permite acceso solo a DELETE
	                
	                
	                auths.requestMatchers(HttpMethod.GET, "/api/posts").hasAuthority("READ");
	                auths.requestMatchers(HttpMethod.GET, "/api/posts-resttemplate").hasAuthority("READ");
	                auths.requestMatchers(HttpMethod.GET, "/api/posts-webclient").hasAuthority("READ");
	                
	                // auths.requestMatchers(HttpMethod.GET, "/api/posts-restTemplate").hasRole("ADMIN");
	                // auths.requestMatchers(HttpMethod.GET, "/api/posts-webclient").hasAuthority("READ");	                	               
	                
	                // Permitir acceso a READ, CREATE y DELETE para ROLE_ADMIN
	                // auths.requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN"); // Solo acceso para ADMIN
					
					// auths.requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER");
					// auths.requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "DEVELOPER", "USER", "GUEST");					
					// auths.requestMatchers(HttpMethod.POST, "/api/**").hasAnyRole("ADMIN", "DEVELOPER");
					// auths.requestMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN", "DEVELOPER");
					// auths.requestMatchers(HttpMethod.DELETE, "/api/**").hasAnyRole("ADMIN");

					// CONFIGURACION POR DEFECTO (PARA LOS NO ESPECIFICADOS)
					auths.anyRequest().authenticated(); // CREDENCIALES VALIDAS, ENTONCES LA RESPUESTA ES 200 (OK)
					// auths.anyRequest().denyAll(); // LA RESPUESTA SIEMPRE SERA 403 (FORBIDDEN)
				})
				// CONFIGURA EL AuthenticationEntryPoint Y EL AccessDeniedHandler CORRECTAMENTE
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.authenticationEntryPoint(this.jwtAuthenticationEntryPoint) // PARA ERRORES 401
						.accessDeniedHandler(this.jwtAccessDeniedHandler) // PARA ERRORES 403
				)
		        // .addFilterBefore(new JwtTokenValidator(this.jwtUtils), UsernamePasswordAuthenticationFilter.class) // DEBIDO A QUE TENEMOS UN LOGIN		        
				// .addFilterBefore(new JwtTokenValidator(this.jwtUtils), UsernamePasswordAuthenticationFilter.class) // NO USAMOS AUTENTICACION BASICA
				.addFilterBefore(new JwtTokenValidator(this.jwtUtils), BasicAuthenticationFilter.class) // NO USAMOS AUTENTICACION BASICA				
				.build();
	}
	// PASO 2: CONFIGURANDO AUTHENTICATION MANAGER
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	// PASO 3: CONFIGURANDO UN PROVIDER EN PARTICULAR
	// EN ESTE CASO, USAREMOS DaoAuthenticationProvider, QUE NECESITA UN
	// PasswordEncoder Y UN UserDetailsService
	// PASO 4: SE CONFIGURO EL UserDetailsService COMO UN SERVICIO CON LA CLASE
	// UserDetailsServiceImpl
	@Bean
	AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(this.passwordEncoder());
		provider.setUserDetailsService(userDetailsService);

		return provider;
	}

	// PASO 4: CONFIGURANDO EL TIPO DE PasswordEncoder A UTILIZAR
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
