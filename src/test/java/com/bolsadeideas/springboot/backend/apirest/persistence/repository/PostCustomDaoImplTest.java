package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadWithUserIdDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)
class PostCustomDaoImplTest {
	
	@Mock
	private EntityManager entityManager;
	
	@Mock
	private TypedQuery<Object[]> typedQuery;
	
	@InjectMocks
	private PostCustomDaoImpl postCustomDaoImpl;
	
	private Validator validator;
	
	@BeforeEach
    void setUp() {
        // Inicializar el Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

	@Test
	void testFindAllPostsWithUserId() {
		// ARRANGE
		// CREACION DE RESULTADOS SIMULADOS
		List<Object[]> mockResults = new ArrayList<>();
		mockResults.add(new Object[]{1L, "Title", "Body", 2L});
		
		// CONFIGURA EL MOCK DE createQuery PARA RESOLVER EL EL MOCK DE TypedQuery
		when(this.entityManager.createQuery(anyString(),eq(Object[].class))).thenReturn(typedQuery);
		
		// CONFIGURA EL MOCK DE TypedQuery PARA QUE getResultList() DEVUELVA mockResults
        when(typedQuery.getResultList()).thenReturn(mockResults);
        
        // ACT
        // LLAMADA AL METODO DE PRUEBA
        List<PostReadWithUserIdDTO> posts = this.postCustomDaoImpl.findAllPostsWithUserId();		
		
        // ASSERT
        // VERIFICACIONES
		assertNotNull(posts);
		assertEquals(1, posts.size());
		assertEquals("Title", posts.get(0).title());
	}
	
	@Test
	void testFindAllPostsWithUserId_emptyResults() {
	    // Arrange
	    List<Object[]> mockResults = new ArrayList<>();
	    when(this.entityManager.createQuery(anyString(), eq(Object[].class))).thenReturn(typedQuery);
	    when(typedQuery.getResultList()).thenReturn(mockResults);
	    
	    // Act
	    List<PostReadWithUserIdDTO> posts = this.postCustomDaoImpl.findAllPostsWithUserId();
	    
	    // Assert
	    assertNotNull(posts);
	    assertTrue(posts.isEmpty(), "La lista de posts debería estar vacía.");
	}
	
	/**
	 * [Objetivo]
	 * La prueba esta diseñada para verificar que el metodo findAllPostsWithUserId() no maneja internamente la excepción y, 
	 * en cambio, la propaga hacia arriba, tal como se espera cuando se produce un error en el getResultList().
	 */
	@Test
	void testFindAllPostsWithUserId_exception() {
	    // Arrange
	    when(this.entityManager.createQuery(anyString(), eq(Object[].class))).thenReturn(typedQuery);
	    when(typedQuery.getResultList()).thenThrow(new RuntimeException("Error en la consulta"));
	    	    
	    // Act & Assert
	    assertThrows(RuntimeException.class, () -> {
	        this.postCustomDaoImpl.findAllPostsWithUserId();
	    });
	}
	
	@Test
	void testFindAllPostsWithUserId_multipleResults() {
	    // Arrange
	    List<Object[]> mockResults = new ArrayList<>();
	    mockResults.add(new Object[]{1L, "Title1", "Body1", 2L});
	    mockResults.add(new Object[]{2L, "Title2", "Body2", 3L});
	    
	    when(this.entityManager.createQuery(anyString(), eq(Object[].class))).thenReturn(typedQuery);
	    when(typedQuery.getResultList()).thenReturn(mockResults);
	    
	    // Act
	    List<PostReadWithUserIdDTO> posts = this.postCustomDaoImpl.findAllPostsWithUserId();
	    
	    // Assert
	    assertNotNull(posts);
	    assertEquals(2, posts.size());
	    assertEquals("Title1", posts.get(0).title());
	    assertEquals("Title2", posts.get(1).title());
	}
	
	@Test
	void testFindAllPostsWithUserId_titleCannotBeNull() {
	    // Arrange
	    List<Object[]> mockResults = new ArrayList<>();
	    mockResults.add(new Object[]{1L, null, "Body", 2L});
	    
	    when(this.entityManager.createQuery(anyString(), eq(Object[].class))).thenReturn(typedQuery);
	    when(typedQuery.getResultList()).thenReturn(mockResults);
	    
	    // Act
	    List<PostReadWithUserIdDTO> posts = this.postCustomDaoImpl.findAllPostsWithUserId();
	    
	    // Assert
	    assertNotNull(posts);
	    assertEquals(1, posts.size());
	    
	    // Verificar violaciones de validación
	    Set<ConstraintViolation<PostReadWithUserIdDTO>> violations = this.validator.validate(posts.get(0));
	    assertFalse(violations.isEmpty());  // Debería haber violaciones
	    assertEquals(1, violations.size());
	    assertNotNull(violations.iterator().next().getMessage());
	}

	@Test
	void testFindAllPostsWithUserId_differentDataTypes() {
	    // Arrange
	    List<Object[]> mockResults = new ArrayList<>();
	    mockResults.add(new Object[]{1L, "Title", "Body", 2L});
	    
	    when(this.entityManager.createQuery(anyString(), eq(Object[].class))).thenReturn(typedQuery);
	    when(typedQuery.getResultList()).thenReturn(mockResults);
	    
	    // Act
	    List<PostReadWithUserIdDTO> posts = this.postCustomDaoImpl.findAllPostsWithUserId();
	    
	    // Assert
	    assertNotNull(posts);
	    assertEquals(1, posts.size());
	    assertEquals(1L, posts.get(0).id());  // Verificar que los datos estén correctamente mapeados
	}
}
