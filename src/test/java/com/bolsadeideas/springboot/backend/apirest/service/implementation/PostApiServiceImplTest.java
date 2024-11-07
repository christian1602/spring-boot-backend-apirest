package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IPostRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadWithUserIdDTO;

@ExtendWith(MockitoExtension.class)
public class PostApiServiceImplTest {

	@Mock
	private IPostRepository postRepository;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private WebClient webClient;
	
	@InjectMocks
	private PostApiServiceImpl postApiServiceImpl;
	
	private String postApiUrl;
	
	@BeforeEach
	void Setup() {
		// DEBIDO A QUE PostApiServiceImpl TIENE ESTE ATRIBUTO
		// @Value("${external.api.url.posts}")
		// private String postApiUrl;
		// DESDE EL TEST, DEBEMOS ASIGNARLE DIRECTAMENTE SU VALOR DE "https://jsonplaceholder.typicode.com"
		ReflectionTestUtils.setField(this.postApiServiceImpl, "postApiUrl", "https://jsonplaceholder.typicode.com");
		
		this.postApiUrl = "https://jsonplaceholder.typicode.com";
	}
	
	@Test
	void testApiFetchPostsRestTemplate() {
		// Arrange
		String url = this.postApiUrl.concat("/posts/");
		
		PostReadWithUserIdDTO[] mockResponse = {
				new PostReadWithUserIdDTO(1L, "Title 1", "Body 1", 1L),
				new PostReadWithUserIdDTO(2L, "Title 2", "Body 2", 2L)
		};
		
		when(this.restTemplate.getForObject(url, PostReadWithUserIdDTO[].class)).thenReturn(mockResponse);
		
		// Act
		List<PostReadWithUserIdDTO> result = this.postApiServiceImpl.apiFetchPostsRestTemplate();
		
		// Assert
		assertNotNull(mockResponse);
		assertEquals(2, result.size());
		assertEquals("Title 1", result.get(0).title());
		verify(this.restTemplate, times(1)).getForObject(url, PostReadWithUserIdDTO[].class);
	}
	
	@Test
	void testApiFindById() {
	    // Arrange
	    Long id = 1L;
	    String url = this.postApiUrl.concat("/posts/").concat(id.toString());
	    PostReadWithUserIdDTO mockResponse = new PostReadWithUserIdDTO(id, "Title 1", "Body 1", 1L);

	    when(restTemplate.getForObject(url, PostReadWithUserIdDTO.class)).thenReturn(mockResponse);

	    // Act
	    PostReadWithUserIdDTO result = this.postApiServiceImpl.apiFindById(id);

	    // Assert
	    assertNotNull(result);
	    assertEquals(id, result.id());
	    assertEquals("Title 1", result.title());
	    verify(restTemplate, times(1)).getForObject(url, PostReadWithUserIdDTO.class);
	}

}
