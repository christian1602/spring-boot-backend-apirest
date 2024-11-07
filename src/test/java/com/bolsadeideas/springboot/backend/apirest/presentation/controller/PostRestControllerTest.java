package com.bolsadeideas.springboot.backend.apirest.presentation.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bolsadeideas.springboot.backend.apirest.exception.advice.GlobalExceptionHandler;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadWithUserIdDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostApiService;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostCustomService;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PostRestControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private IPostService postService;
	
	@Mock
	private IPostApiService postApiService;
	
	@Mock
	private IPostCustomService postCustomService;
	
	@InjectMocks
	private PostRestController postRestController;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.postRestController)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}
	
	private ObjectMapper objectMapper = new ObjectMapper();
		
	@Test
	void testFindAllPostsWithUserId() throws Exception {
		// Arrange
		List<PostReadWithUserIdDTO> posts = List.of(
				new PostReadWithUserIdDTO(1L,"Title 1","Body 1",1L),
				new PostReadWithUserIdDTO(2L,"Title 2","Body 2",2L));
		
		when(this.postCustomService.getAllPostsWithUserId()).thenReturn(posts);
		
		// Act & Assert
		this.mockMvc.perform(get("/api/posts-with-userid")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(1L))
			.andExpect(jsonPath("$[0].title").value("Title 1"))
			.andExpect(jsonPath("$[0].body").value("Body 1"))
            .andExpect(jsonPath("$[0].userId").value(1L))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].title").value("Title 2"))
            .andExpect(jsonPath("$[1].body").value("Body 2"))
            .andExpect(jsonPath("$[1].userId").value(2L));
	}
	
	@Test
	void testIndex() throws Exception {
		// Arrange
		PostReadDTO postReadDTO = new PostReadDTO(1L,"Test Title","Test Body");
		List<PostReadDTO> posts = List.of(postReadDTO);
	
		when(this.postService.findAll()).thenReturn(posts);
		
		// Act & Assert
		this.mockMvc.perform(get("/api/posts"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].title").value("Test Title"))
			.andExpect(jsonPath("$[0].body").value("Test Body"));
	}
	
	@Test
	void testShow() throws Exception {
		// Arrange
		PostReadDTO post = new PostReadDTO(1L,"Test Title","Test Body");
		
		when(this.postService.findById(anyLong())).thenReturn(post);
		
		// Act & Assert
		this.mockMvc.perform(get("/api/posts/{id}",1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Test Title"))
			.andExpect(jsonPath("$.body").value("Test Body"));
	}
	
	@Test
	void testCreate() throws Exception {	
		// Arrange
		PostWriteDTO postWriteDTO = new PostWriteDTO("New Title","New Body",1L);
		PostReadDTO newPostReadDTO = new PostReadDTO(1L,"New Title","New Body");
		
		when(this.postService.save(postWriteDTO)).thenReturn(newPostReadDTO);
		
		// Act & Assert
		this.mockMvc.perform(post("/api/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(postWriteDTO)))				
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.data.title").value("New Title"))
			.andExpect(jsonPath("$.data.body").value("New Body"))
			.andExpect(jsonPath("$.message").value("¡El Post ha sido creado con éxito!"));
	}
	
	@Test
	void testCreate_InvalidDataException() throws Exception {		
	    // Arrange: Crear un objeto de datos invalidos (por ejemplo, un título vacío)
	    PostWriteDTO invalidPostWriteDTO = new PostWriteDTO("", "New Body", 1L);

	    // Act & Assert: Verificar que se lanza la excepción InvalidDataException
	    this.mockMvc.perform(post("/api/posts")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(this.objectMapper.writeValueAsString(invalidPostWriteDTO)))
	        .andExpect(status().isBadRequest()) 						// Verificar que el estado es 400 (Bad Request)
	    	.andExpect(jsonPath("$.message").value("Invalid Data")); 	// O el mensaje adecuado que tu manejador de excepciones este devolviendo	        
	}
	
	@Test
	void testUpdate() throws Exception {
		// Arrange
		PostWriteDTO postWriteDTO = new PostWriteDTO("Updated Title","Updated Body",1L);
		PostReadDTO updatedPostReadDTO = new PostReadDTO(1L,"Updated Title","Updated Body");
		
		// TODOS LOS ARGUMENTOS DEL METODO DEBEN SER MATCHERS O
		// DEBEN SER VALORES ESPECIFICOS, PERO NUNCA COMBINADOS
		when(this.postService.update(anyLong(), eq(postWriteDTO))).thenReturn(updatedPostReadDTO);
		
		// Act & Assert
		this.mockMvc.perform(put("/api/posts/{id}",1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(postWriteDTO)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.title").value("Updated Title"))
			.andExpect(jsonPath("$.data.body").value("Updated Body"))
			.andExpect(jsonPath("$.message").value("¡El Post ha sido actualizado con éxito!"));
	}
	
	@Test
	void testDelete() throws Exception {
		// Arrange
		doNothing().when(this.postService).delete(anyLong());
		
		// Act & Assert
		this.mockMvc.perform(delete("/api/posts/{id}",1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("¡El Post ha sido eliminado con éxito!"));
	}
}
