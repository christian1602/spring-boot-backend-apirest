package com.bolsadeideas.springboot.backend.apirest.presentation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.bolsadeideas.springboot.backend.apirest.exception.InvalidDataException;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadWithUserIdDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostApiService;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostCustomService;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200 " })
public class PostRestController {

	private final IPostService postService;
	private final IPostApiService postApiService;
	private final IPostCustomService postCustomService;

	public PostRestController(IPostService postService, IPostApiService postApiService,
			IPostCustomService postCustomService

	) {
		this.postService = postService;
		this.postApiService = postApiService;
		this.postCustomService = postCustomService;
	}

	/*
	@GetMapping("/sync-posts")
	public ResponseEntity<?> syncPosts() {
		List<PostDTO> posts = this.postApiService.fetchPostsRestTemplate();

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
	*/
	
	// USANDO LA INTERFACE IPostCustomService
	
	@GetMapping("/posts-with-userid")
	public List<PostReadWithUserIdDTO> findAllPostsWithUserId() {
		return this.postCustomService.getAllPostsWithUserId();
	}
	
	// USANDO LA INTERFACE IPostApiService
	
	@GetMapping("/posts-api-resttemplate")
	public List<PostReadWithUserIdDTO> listPostsRestTemplate() {
		return this.postApiService.apiFetchPostsRestTemplate();
	}
	
	@GetMapping("/posts-api-webclient")
	public List<PostReadWithUserIdDTO> listPosts() {
		return this.postApiService.apiFetchPostsWebClient();
	}
	
	@GetMapping("/posts-api/{id}")
	public ResponseEntity<?> showApi(@PathVariable Long id) {
		PostReadWithUserIdDTO postReadWithUserIdDTO = this.postApiService.apiFindById(id);
		return new ResponseEntity<PostReadWithUserIdDTO>(postReadWithUserIdDTO, HttpStatus.OK);
	}

	// USANDO LA INTERFACE IPostService
	
	@GetMapping("/posts")
	public List<PostReadDTO> index() {
		return this.postService.findAll();
	}

	@GetMapping("/posts/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		PostReadDTO postReadDTO = this.postService.findById(id);
		return new ResponseEntity<PostReadDTO>(postReadDTO, HttpStatus.OK);
	}

	@PostMapping("/posts")
	public ResponseEntity<?> create(@Valid @RequestBody PostWriteDTO postWriteDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
 
		PostReadDTO nuevoPostReadDTO = this.postService.save(postWriteDTO);

		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El Post ha sido creado con éxito!");
		response.put("post", nuevoPostReadDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/posts/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody PostWriteDTO postWriteDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		
		PostReadDTO updatedPostReadDTO = this.postService.update(id, postWriteDTO);

		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El Post ha sido actualizado con éxito!");
		response.put("post", updatedPostReadDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/posts/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {		
		this.postService.delete(id);
		
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El Post ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
