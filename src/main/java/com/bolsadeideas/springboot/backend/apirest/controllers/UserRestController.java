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

import com.bolsadeideas.springboot.backend.apirest.models.entity.User;
import com.bolsadeideas.springboot.backend.apirest.models.services.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200 " })
public class UserRestController {

	private final IUserService userService;	

	public UserRestController(IUserService userService) {
		this.userService = userService;		
	}

	@GetMapping("/users")
	public List<User> index() {
		return this.userService.findAll();
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		User user = null;

		Map<String, Object> response = new HashMap<>();

		try {
			user = this.userService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (user == null) {
			response.put("mensaje",
					"El User con el ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@PostMapping("/users")
	public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
		User nuevoUser = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("errors", errors);
		}

		try {
			nuevoUser = this.userService.save(user);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Usuario ha sido creado con éxito!");
		response.put("user", nuevoUser);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody User user, BindingResult result, @PathVariable Long id) {
		User userActual = this.userService.findById(id);
		User userActualizado = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (userActual == null) {
			response.put("mensaje", "Error: No se pudo editar, el User con el ID: ".concat(id.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			userActual.setName(user.getName());
			userActual.setUsername(user.getUsername());
			userActual.setEmail(user.getEmail());

			userActualizado = this.userService.save(userActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el User en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El User ha sido actualizado con éxito!");
		response.put("post", userActualizado);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		User user = this.userService.findById(id);

		if (user == null) {
			response.put("mensaje", "Error: no se pudo eliminar, el User con ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			this.userService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el User de la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El User ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
