package com.bolsadeideas.springboot.backend.apirest.presentation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
	
	private static String jwtToken;
	private static Long idUserTest;

	// [PRUEBA DE INTEGRACION]
	// USA @SpringBootTest Y @AutoConfigureMockMvc PARA INYECTAR MockMvc EN UN CONTEXTO DE APLICACION COMPLETO, 
	// PERMITIENDO PROBAR COMO INTERACTUA EL CONTROLADOR CON LOS DEMAS COMPONENTES
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private IUserRepository userRepository;
		
	private String usernameTest;
	private String passwordTest;
	private String emailTest;
	private String roleTest;

	@BeforeEach
	void setup() throws Exception {
		this.usernameTest = "test_user";
		this.passwordTest = "123456";
		this.emailTest = "test_user@mail";
		this.roleTest = "ADMIN";
		
		if (PostRestControllerIntegrationTest.jwtToken == null) {
			if (!this.userRepository.existsByEmail(this.emailTest)) {
				this.registerTestUser(this.usernameTest, this.passwordTest);				
			}
			
			MvcResult mvcResult = this.loginTestUser(this.usernameTest, this.passwordTest);
			PostRestControllerIntegrationTest.jwtToken = this.obtainAccessToken(mvcResult);
			
			Optional<UserEntity> optionaUserEntityTop = this.userRepository.findTopByOrderByIdDesc();
			PostRestControllerIntegrationTest.idUserTest = optionaUserEntityTop.get().getId();
			this.loadInitPosts();
		}
	}
	
	private void registerTestUser(String username, String password) throws Exception{
		// REGISTRAR Y AUTENTICAR UN USUARIO PARA OBTENER UN JWT
		AuthRolesDTO authRolesDTO = new AuthRolesDTO(List.of(this.roleTest));
		UserWriteDTO userWriteDTO = new UserWriteDTO(this.emailTest, username, password, authRolesDTO);

		// REGISTRAR UN USUARIO (SI AUN NO EXISTE)
		this.mockMvc.perform(post("/auth/sign-up")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(userWriteDTO)))
		.andExpect(status().isCreated());		
	}
	
	private MvcResult loginTestUser(String username, String password) throws Exception {
		AuthLoginDTO authLoginDTO = new AuthLoginDTO(username, password);

		return this.mockMvc.perform(post("/auth/log-in")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authLoginDTO)))
		.andExpect(status().isOk())
		.andReturn();
	}

	private String obtainAccessToken(MvcResult mvcResult) throws Exception {
		String response = mvcResult.getResponse().getContentAsString();
		return JsonPath.parse(response).read("$.access_token");
	}
	
	private void loadInitPosts() throws Exception {
		PostWriteDTO postWriteDTO = new PostWriteDTO("Titulo Post 1","Body Post 1",PostRestControllerIntegrationTest.idUserTest);
		
		this.mockMvc.perform(post("/api/posts")
				.header("Authorization", "Bearer ".concat(PostRestControllerIntegrationTest.jwtToken))
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(postWriteDTO)))
		.andExpect(status().isCreated());		
	}

	@Test
	public void testIndex() throws Exception {
		this.mockMvc.perform(get("/api/posts")
				.header("Authorization", "Bearer ".concat(PostRestControllerIntegrationTest.jwtToken)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$",hasSize(greaterThan(0))));
	}
	
	@Test
	public void testCreate() throws Exception {
		PostWriteDTO postWriteDTO = new PostWriteDTO("Titulo del Post", "Contenido del Post", 1L);
		
		this.mockMvc.perform(post("/api/posts")
				.header("Authorization", "Bearer ".concat(PostRestControllerIntegrationTest.jwtToken))
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(postWriteDTO)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.data.title").value("Titulo del Post"));
	}
}
