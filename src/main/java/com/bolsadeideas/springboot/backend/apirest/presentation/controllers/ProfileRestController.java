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

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProfileEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProfileService;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" })
public class ProfileRestController {

	private final IProfileService profileService;
	private final IUserService userService;

	public ProfileRestController(IProfileService profileService, IUserService userService) {
		this.profileService = profileService;
		this.userService = userService;
	}

	@GetMapping("/profiles")
	public List<ProfileEntity> index() {
		return this.profileService.findAll();
	}

	@GetMapping("/profiles/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		ProfileEntity perfil = null;

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

		return new ResponseEntity<ProfileEntity>(perfil, HttpStatus.OK);
	}

	@PostMapping("/profiles")
	public ResponseEntity<?> create(@Valid @RequestBody ProfileEntity profile, BindingResult result) {
		ProfileEntity nuevoPerfil = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}		
		
		try {
			// RECUPEAR EL USER DESDE LA BASE DE DATOS			
			// UserEntity userEncontrado = this.userService.findById(profile.getUser().getId());
			UserEntity userEncontrado = null;
			
			if (userEncontrado == null) {
				response.put("mensaje", "Error: No se pudo crear el perfil para el usuario con el ID: ".concat(profile.getUser().getId().toString())
						.concat(" porque no existe en la base de datos"));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}	
			
			// TODO: VALIDAR QUE EL USUARIO NO ESTE YA ASOCIADO AL PERFIL
			profile.setUser(userEncontrado);
			nuevoPerfil = this.profileService.save(profile);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El perfil ha sido creado con éxito!");
		response.put("perfil", nuevoPerfil);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/profiles/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody ProfileEntity profile, BindingResult result, @PathVariable Long id) {
		ProfileEntity perfilActual = this.profileService.findById(id);
		ProfileEntity perfilActualizado = null;

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

		try {
			// RECUPEAR EL USER DESDE LA BASE DE DATOS			
			// UserEntity userEncontrado = this.userService.findById(profile.getUser().getId());
			UserEntity userEncontrado = null;
			
			if (userEncontrado == null) {
				response.put("mensaje", "Error: No se pudo editar el perfil para el usuario con el ID: ".concat(profile.getUser().getId().toString())
						.concat(" porque no existe en la base de datos"));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}	
			
			// TODO: VALIDAR QUE EL USUARIO NO ESTE YA ASOCIADO AL PERFIL.
			perfilActual.setBio(profile.getBio());
			perfilActual.setWebsite(profile.getWebsite());			
			perfilActual.setUser(userEncontrado);

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

		ProfileEntity perfil = this.profileService.findById(id);
		
		if (perfil == null) {
			response.put("mensaje", "Error: no se pudo eliminar, el Profile con ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			this.profileService.delete(id);			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el Perfil de la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Perfil ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
