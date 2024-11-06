package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bolsadeideas.springboot.backend.apirest.exception.PostNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.mappers.PostReadMapper;
import com.bolsadeideas.springboot.backend.apirest.mappers.PostWriteMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PostEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IPostRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IUserRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostWriteDTO;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

	@InjectMocks
	private PostServiceImpl postServiceImpl;

	@Mock
	private IPostRepository postRepository;

	@Mock
	private IUserRepository userRepository;

	@Mock
	private PostReadMapper postReadMapper;

	@Mock
	private PostWriteMapper postWriteMapper;

	private PostEntity postEntity;
	private PostReadDTO postReadDTO;
	private PostWriteDTO postWriteDTO;
	private UserEntity userEntity;

	@BeforeEach
	void setUp() {
		this.userEntity = new UserEntity();
		this.userEntity.setId(1L);

		this.postEntity = new PostEntity();
		this.postEntity.setId(1L);
		this.postEntity.setUser(userEntity);

		this.postReadDTO = new PostReadDTO(1L, "Existing Title", "Existing Body");
		this.postWriteDTO = new PostWriteDTO("New Title", "New Body", 1L);
	}

	@Test
	void testFindAll() {
		// Arrange
		when(postRepository.findAll()).thenReturn(List.of(postEntity));
		when(this.postReadMapper.toPostReadDTO(any(PostEntity.class))).thenReturn(this.postReadDTO);

		// Act
		List<PostReadDTO> result = this.postServiceImpl.findAll();

		// Assert
		assertEquals(1, result.size());
		verify(this.postRepository).findAll();
	}
	
	@Test
	void testFindById() {
		// Arrange
		when(this.postRepository.findById(anyLong())).thenReturn(Optional.of(this.postEntity));
		// NO ES NECESARIO USAR eq(this.postEntity) SI YA ESTAS PASANDO DIRECTANENTE EL VALOR EXACTO (this.postEntity) AL METODO
		when(this.postReadMapper.toPostReadDTO(this.postEntity)).thenReturn(this.postReadDTO);
		
		// Act
		// SE COLOCA 1L PORQUE ESTAMOS PROBANDO EL COMPORTAMIENTO REAL DEL METODO
		PostReadDTO result = this.postServiceImpl.findById(1L);
		
		// Assert
		assertNotNull(result);
		verify(this.postRepository).findById(anyLong());
	}
	
	
	/**
	 * [Objetivo]
	 * Validar que la excepción PostNotFoundException es lanzada cuando no se encuentra un post en la base de datos.
	 */
	@Test
	void testFindById_PostNotFoundException() {
		// Arrange
		when(this.postRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		// Act & Assert
		assertThrows(PostNotFoundException.class, () -> this.postServiceImpl.findById(1L));
	}
	/**
	 * [Consideraciones]
	 * 
	 * En este caso, se pasan objetos específicos a los mappers para verificar que estan recibiendo los datos correctos. 
	 * Esto mejora la precision y claridad del test y asegura que el comportamiento esperado se este cumpliendo con los datos específicos, 
	 * en lugar de permitir que se pase cualquier objeto genérico (any()).
	 * 
	 * Si usas any() en lugar de los objetos especificos, no estarias verificando que los mappers reciban los objetos correctos que se les pasan desde el servicio.
	 */
	@Test
	void testSave() {
		// Arrange
		when(this.userRepository.findById(anyLong())).thenReturn(Optional.of(this.userEntity));
		when(this.postWriteMapper.toPostEntity(this.postWriteDTO)).thenReturn(this.postEntity);
		when(this.postRepository.save(any(PostEntity.class))).thenReturn(this.postEntity);
		when(this.postReadMapper.toPostReadDTO(this.postEntity)).thenReturn(this.postReadDTO);
		
		// Acta
		PostReadDTO result = this.postServiceImpl.save(this.postWriteDTO);
		
		// Asserts
		assertNotNull(result);
		verify(this.postRepository).save(any(PostEntity.class));
		verify(this.userRepository).findById(anyLong());
	}
}