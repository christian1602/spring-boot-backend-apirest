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

import com.bolsadeideas.springboot.backend.apirest.models.entity.Profile;
import com.bolsadeideas.springboot.backend.apirest.models.entity.User;
import com.bolsadeideas.springboot.backend.apirest.models.services.IProfileService;
import com.bolsadeideas.springboot.backend.apirest.models.services.IUserService;
import com.bolsadeideas.springboot.backend.apirest.models.services.IUserValidationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" })
public class ProfileRestController {

	private final IProfileService profileService;
	private final IUserService userService;
	private final IUserValidationService userValidationService;

	public ProfileRestController(IProfileService profileService, IUserService userService, IUserValidationService userValidationService) {
		this.profileService = profileService;
		this.userService = userService;
		this.userValidationService = userValidationService;
	}

	@GetMapping("/profiles")
	public List<Profile> index() {
		return this.profileService.findAll();
	}

	@GetMapping("/profiles/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Profile perfil = null;

		Map<String, Object> response = new HashMap<>();

		try {
			perfil = this.profileService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (perfil == null) {
			response.put("mensaje",
					"El Profile con el ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Profile>(perfil, HttpStatus.OK);
	}

	@PostMapping("/profiles")
	public ResponseEntity<?> create(@Valid @RequestBody Profile profile, BindingResult result) {
		Profile nuevoPerfi = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		ResponseEntity<?> validationResponse = this.userValidationService.validateUser(profile.getUser(), response);
		if (validationResponse != null) {
			return validationResponse;
		}
		
		try {
			nuevoPerfi = this.profileService.save(profile);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Profile ha sido creado con éxito!");
		response.put("perfil", nuevoPerfi);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/profiles/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Profile profile, BindingResult result, @PathVariable Long id) {
		Profile perfilActual = this.profileService.findById(id);
		Profile perfilActualizado = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (perfilActual == null) {
			response.put("mensaje", "Error: No se pudo editar, el Profile con el ID: ".concat(id.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		ResponseEntity<?> validationResponse = this.userValidationService.validateUser(profile.getUser(), response);
		if (validationResponse != null) {
			return validationResponse;
		}

		try {
			User usuario = new User();
			usuario.setId(profile.getUser().getId());

			perfilActual.setBio(profile.getBio());
			perfilActual.setWebsite(profile.getWebsite());
			perfilActual.setUser(usuario);

			perfilActualizado = this.profileService.save(perfilActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el cliente en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Profile ha sido actualizado con éxito!");
		response.put("perfil", perfilActualizado);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/profiles/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		Profile perfil = this.profileService.findById(id);
		System.out.println("perfil encontrado => " + perfil);
		
		if (perfil == null) {
			response.put("mensaje", "Error: no se pudo eliminar, el Profile con ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			// SOLO EN RELACIONES DE ONE TOM ONE
			// Romper la relacion entre el User y el Profile
			User user = perfil.getUser();
			
			if (user != null) {
				user.setProfile(null); // Rompe la referencia
				this.userService.save(user); // Guarda el User actualizado sin Profile
				
			}
			this.profileService.delete(id);			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el Perfil de la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Profile ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
