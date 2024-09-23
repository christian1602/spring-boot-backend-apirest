package com.bolsadeideas.springboot.backend.apirest.controllers;

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

import com.bolsadeideas.springboot.backend.apirest.models.entity.Post;
import com.bolsadeideas.springboot.backend.apirest.models.services.IPostApiService;
import com.bolsadeideas.springboot.backend.apirest.models.services.IPostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200 " })
public class PostRestController {

	private final IPostService postService;	
	private final IPostApiService postApiService;

	public PostRestController(IPostService postService, IPostApiService postApiService) {
		this.postService = postService;
		this.postApiService = postApiService;
	}
		
	
	@GetMapping("/sync-posts")
	public ResponseEntity<?> syncPosts(){
		List<Post> posts = this.postApiService.fetchPosts();
		
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
	public List<Post> index() {
		return this.postService.findAll();
	}

	@GetMapping("/posts/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Post post = null;

		Map<String, Object> response = new HashMap<>();

		try {
			post = this.postService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (post == null) {
			response.put("mensaje",
					"El cliente con el ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Post>(post, HttpStatus.OK);
	}

	@PostMapping("/posts")
	public ResponseEntity<?> create(@Valid @RequestBody Post post, BindingResult result) {
		Post nuevoPost = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("errors", errors);
		}

		try {
			nuevoPost = this.postService.save(post);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Post ha sido creado con éxito!");
		response.put("cliente", nuevoPost);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/posts/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Post post, BindingResult result, @PathVariable Long id) {
		Post postActual = this.postService.findById(id);
		Post postActualizado = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (postActual == null) {
			response.put("mensaje", "Error: No se pudo editar, el Post con el ID: ".concat(id.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			postActual.setUserId(post.getUserId());
			postActual.setTitle(post.getTitle());
			postActual.setBody(post.getBody());

			postActualizado = this.postService.save(postActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el Post en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Post ha sido actualizado con éxito!");
		response.put("post", postActualizado);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/cllientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		Post post = this.postService.findById(id);

		if (post == null) {
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

		response.put("mensaje", "¡El cliente ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
