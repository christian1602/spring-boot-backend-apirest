package com.bolsadeideas.springboot.backend.apirest.presentation.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PostEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UserDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostApiService;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostCustomService;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostService;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200 " })
public class PostRestController {

	private final IPostService postService;		
	private final IPostApiService postApiService;
	private final IPostCustomService postCustomService;
	private final IUserService userService;	

	public PostRestController(
			IPostService postService, 
			IPostApiService postApiService,
			IPostCustomService postCustomService,
			IUserService userService
			
	) {
		this.postService = postService;
		this.postApiService = postApiService;			
		this.postCustomService = postCustomService;
		this.userService = userService;		
	}
	
	@GetMapping("/sync-posts")
	public ResponseEntity<?> syncPosts(){
		List<PostEntity> posts = this.postApiService.fetchPosts();
		
		Map<String, Object> response = new HashMap<>();
		
		if (posts.size() == 0) {
			response.put("mensaje", "La lista de Posts se encuentra vacía");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
		}
		
		try {
			this.postApiService.saveAll(posts);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert masivo de la lista de Posts en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La sincronización de Posts fue realizada éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/posts")
	public List<PostDTO> index() {
		return this.postService.findAll();
	}
	
	@GetMapping("/posts-with-userid")
	public List<PostDTO> findAllPostsWithUserId() {
		return this.postCustomService.getAllPostsWithUserId();
	}

	@GetMapping("/posts/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		PostDTO postDTO = null;

		Map<String, Object> response = new HashMap<>();

		try {
			postDTO = this.postService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (postDTO == null) {
			response.put("mensaje",
					"El cliente con el ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<PostDTO>(postDTO, HttpStatus.OK);
	}

	@PostMapping("/posts")
	public ResponseEntity<?> create(@Valid @RequestBody PostDTO postDTO, BindingResult result) {
		PostDTO nuevoPostDTO = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("errors", errors);
		}		

		try {
			// RECUPEAR EL USER DESDE LA BASE DE DATOS		
			UserDTO userDTOEncontrado = this.userService.findById(postDTO.getUserId());
			
			if (userDTOEncontrado == null) {
				response.put("mensaje", "Error: No se pudo crear el post para el usuario con el ID: ".concat(postDTO.getUserId().toString())
						.concat(" porque no existe en la base de datos"));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}	
			
			// TODO: VALIDAR QUE EL USUARIO NO ESTE YA ASOCIADO AL POST
			// postDTO.setUser(userDTOEncontrado);			
			nuevoPostDTO = this.postService.save(postDTO);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Post ha sido creado con éxito!");
		response.put("post", nuevoPostDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/posts/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody PostDTO postDTO, BindingResult result, @PathVariable Long id) {
		PostDTO postDTOActual = this.postService.findById(id);
		PostDTO postDTOActualizado = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (postDTOActual == null) {
			response.put("mensaje", "Error: No se pudo editar, el Post con el ID: ".concat(id.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			// RECUPEAR EL USER DESDE LA BASE DE DATOS
			UserDTO userDTOEncontrado = this.userService.findById(postDTO.getUserId());
			
			if (userDTOEncontrado == null) {
				response.put("mensaje", "Error: No se pudo crear el post para el usuario con el ID: ".concat(postDTO.getUserId().toString())
						.concat(" porque no existe en la base de datos"));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}	
			
			// TODO: VALIDAR QUE EL USUARIO NO ESTE YA ASOCIADO AL POST
			postDTOActual.setTitle(postDTO.getTitle());
			postDTOActual.setBody(postDTO.getBody());
			postDTOActual.setUserId(postDTO.getUserId());
			// postDTOActual.setUser(userDTOEncontrado);

			postDTOActualizado = this.postService.save(postDTOActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el Post en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Post ha sido actualizado con éxito!");
		response.put("post", postDTOActualizado);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/posts/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		PostDTO postDTO = this.postService.findById(id);

		if (postDTO == null) {
			response.put("mensaje", "Error: no se pudo eliminar, el Post con ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			this.postService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el Post de la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Post ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
