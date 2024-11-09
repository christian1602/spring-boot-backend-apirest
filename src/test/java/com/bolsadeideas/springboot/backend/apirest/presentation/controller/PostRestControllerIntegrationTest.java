package com.bolsadeideas.springboot.backend.apirest.presentation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IUserRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthLoginDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthRolesDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UserWriteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 *
 * [Sobre la Biblioteca Hamcrest] Hamcrest es una biblioteca ampliamente
 * utilizada para realizar verificaciones en pruebas. Proporciona una serie de
 * matchers (comparadores) que permiten expresar expectativas de una manera mas
 * legible, y es compatible con herramientas de prueba en Java como JUnit y
 * MockMvc.
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PostRestControllerIntegrationTest {
	
	private static final String USERNAME_TEST = "test_user";
	private static final String PASSWORD_TEST = "123456";
	private static final String EMAIL_TEST = "test_user@mail";
	private static final String ROLES_TEST = "ADMIN";
	
	private static final String API_POSTS_WITH_USER_ID_URL = "/api/posts-with-user-id";
	private static final String API_POSTS_REST_TEMPLATE_URL = "/api/posts-api-rest-template";
	private static final String API_POSTS_WEB_CLIENTE_URL = "/api/posts-api-web-client";
	
	private static final String API_POSTS_URL = "/api/posts";
	private static final String AUTH_LOG_IN_URL = "/auth/log-in";
	private static final String AUTH_SIGN_UP_URL = "/auth/sign-up";
	
	private Long idTopUserTest;
	private String jwtTokenTest;
	private String bearerToken;

	// [PRUEBA DE INTEGRACION]
	// USA @SpringBootTest Y @AutoConfigureMockMvc PARA INYECTAR MockMvc EN UN CONTEXTO DE APLICACION COMPLETO, 
	// PERMITIENDO PROBAR COMO INTERACTUA EL CONTROLADOR CON LOS DEMAS COMPONENTES
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private IUserRepository userRepository;

	@BeforeEach
	void setup() throws Exception {		
		this.setupTestUser();
		this.obtainTestJwtToken();
	}
	
	private void setupTestUser() throws Exception {
	    if (!this.userRepository.existsByEmail(EMAIL_TEST)) {
	        this.registerTestUser();
	    }
	}
	
	private void obtainTestJwtToken() throws Exception {
		if (this.jwtTokenTest == null) {
			MvcResult mvcResult = this.loginTestUser();
			this.jwtTokenTest = this.getAccessToken(mvcResult);
			this.bearerToken = "Bearer ".concat(this.jwtTokenTest);
			this.setIdTopUserTest();
			this.loadInitialPosts();
		}
	}
	
	private void setIdTopUserTest() throws Exception {
		Optional<UserEntity> optionaUserEntityTop = this.userRepository.findTopByOrderByIdDesc();
		this.idTopUserTest = optionaUserEntityTop.get().getId();
	}
	
	private void registerTestUser() throws Exception {
		// REGISTRAR Y AUTENTICAR UN USUARIO PARA OBTENER UN JWT
		AuthRolesDTO authRolesDTO = new AuthRolesDTO(List.of(ROLES_TEST));
		UserWriteDTO userWriteDTO = new UserWriteDTO(EMAIL_TEST, USERNAME_TEST, PASSWORD_TEST, authRolesDTO);

		// REGISTRAR UN USUARIO (SI AUN NO EXISTE)
		this.mockMvc.perform(post(AUTH_SIGN_UP_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(userWriteDTO)))
		.andExpect(status().isCreated());		
	}
	
	private MvcResult loginTestUser() throws Exception {
		AuthLoginDTO authLoginDTO = new AuthLoginDTO(USERNAME_TEST, PASSWORD_TEST);

		return this.mockMvc.perform(post(AUTH_LOG_IN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authLoginDTO)))
		.andExpect(status().isOk())
		.andReturn();
	}

	private String getAccessToken(MvcResult mvcResult) throws Exception {
		String response = mvcResult.getResponse().getContentAsString();
		return JsonPath.parse(response).read("$.access_token");
	}
	
	private void loadInitialPosts() throws Exception {
		PostWriteDTO firstPostWriteDTO = new PostWriteDTO("Titulo Post 1","Body Post 1",this.idTopUserTest);
		PostWriteDTO secondPostWriteDTO = new PostWriteDTO("Titulo Post 2","Body Post 2",this.idTopUserTest);
		
		this.createPost(firstPostWriteDTO);
		this.createPost(secondPostWriteDTO);
	}
	
	private void createPost(PostWriteDTO postWriteDto) throws Exception {
	    this.mockMvc.perform(post(API_POSTS_URL)
	            .header("Authorization", this.bearerToken)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(this.objectMapper.writeValueAsString(postWriteDto)))
	    .andExpect(status().isCreated());
	}
	
	@Test
	public void testFindAllPostsWithUserId() throws Exception {
		// Arrange & Act
		ResultActions resultActions = this.mockMvc.perform(get(API_POSTS_WITH_USER_ID_URL)
				.header("Authorization", this.bearerToken));
		
		// Assert
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",hasSize(greaterThan(0))));
	}
	
	@Test
	public void testListPostsRestTemplate() throws Exception {
		// Arrange & Act
		ResultActions resultActions = this.mockMvc.perform(get(API_POSTS_REST_TEMPLATE_URL)
				.header("Authorization", this.bearerToken));
		
		// Assert
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",hasSize(greaterThan(0))));
	}
	
	@Test
	public void testListPosts() throws Exception {
		// Arrange & Act
		ResultActions resultActions = this.mockMvc.perform(get(API_POSTS_WEB_CLIENTE_URL)
				.header("Authorization", this.bearerToken));
		
		// Assert
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",hasSize(greaterThan(0))));
	}	

	@Test
	public void testIndex() throws Exception {
		// Arrange & Act
		ResultActions resultActions = this.mockMvc.perform(get(API_POSTS_URL)
				.header("Authorization", this.bearerToken));
		
		// Assert
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",hasSize(greaterThan(0))));
	}
	
	@Test
	public void testShow() throws Exception {
		// Arrange
		Long postId = 1L;	// USAR UN ID VALIDO QUE EXISTA EL BASE DE DATOS
				
		// Act
		ResultActions resultActions = this.mockMvc.perform(get(API_POSTS_URL.concat("/{id}"),postId)
				.header("Authorization", this.bearerToken));
		
	    // String responseContent = resultActions.andReturn().getResponse().getContentAsString();
	    // System.out.println("CONTENIDO DE LA RESPUESTA: " + responseContent);
	    	    
		// Assert
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(postId));
	}
	
	@Test
	public void testCreate() throws Exception {
		// Arrange
		PostWriteDTO postWriteDTO = new PostWriteDTO("Titulo del Post", "Contenido del Post", 1L);
		
		// Act
		ResultActions resultActions = this.mockMvc.perform(post(API_POSTS_URL)
				.header("Authorization", this.bearerToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(postWriteDTO)));
		
		// Assert
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.data.title").value("Titulo del Post"));
	}
	
	@Test
	public void testUpdate() throws Exception {
		// Arrange
		Long postId = 1L;	// USAR UN ID VALIDO QUE EXISTA EL BASE DE DATOS
		PostWriteDTO postWriteDTO = new PostWriteDTO("Titulo actualizado", "Cuerpo actualizado", this.idTopUserTest);
		
		// Act
		ResultActions resultActions = this.mockMvc.perform(put(API_POSTS_URL.concat("/{id}"),postId)
				.header("Authorization", this.bearerToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(postWriteDTO)));
		
		// Assert
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.title").value("Titulo actualizado"))
			.andExpect(jsonPath("$.data.body").value("Cuerpo actualizado"));
	}
	
	@Test
	public void testDelete() throws Exception {
		// Arrange
		Long postId = 2L; // USAR UN ID VALIDO QUE EXISTA EL BASE DE DATOS
		
		// Act
		ResultActions resultActions = this.mockMvc.perform(delete(API_POSTS_URL.concat("/{id}"),postId)
				.header("Authorization", this.bearerToken));
		
		// Assert
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("¡El Post ha sido eliminado con éxito!"));
	}
}
