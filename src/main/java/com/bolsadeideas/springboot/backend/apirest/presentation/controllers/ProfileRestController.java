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

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UserDTO;
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
	public List<ProfileDTO> index() {
		return this.profileService.findAll();
	}

	@GetMapping("/profiles/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		ProfileDTO perfilDTO = null;

		Map<String, Object> response = new HashMap<>();

		try {
			perfilDTO = this.profileService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (perfilDTO == null) {
			response.put("mensaje",
					"El Profile con el ID: ".concat(id.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<ProfileDTO>(perfilDTO, HttpStatus.OK);
	}

	@PostMapping("/profiles")
	public ResponseEntity<?> create(@Valid @RequestBody ProfileDTO profileDTO, BindingResult result) {
		ProfileDTO nuevoPerfilDTO = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		UserDTO userDTOEncontrado = null;
		
		try {
			// RECUPEAR EL USER DESDE LA BASE DE DATOS 
			userDTOEncontrado = this.userService.findById(profileDTO.getUserId());
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (userDTOEncontrado == null) {
			response.put("mensaje", "Error: No se pudo crear el perfil para el usuario con el ID: ".concat(profileDTO.getUserId().toString())
					.concat(" porque no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			// TODO: VALIDAR QUE EL USUARIO NO ESTE YA ASOCIADO AL PERFIL
			nuevoPerfilDTO = this.profileService.save(profileDTO);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El perfil ha sido creado con éxito!");
		response.put("perfil", nuevoPerfilDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/profiles/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody ProfileDTO profileDTO, BindingResult result, @PathVariable Long id) {
		ProfileDTO perfilActualDTO = this.profileService.findById(id);
		ProfileDTO perfilActualizadoDTO = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(fieldError -> "El campo '"
					.concat(fieldError.getField()).concat("' ").concat(fieldError.getDefaultMessage()))
					.collect(Collectors.toList());

			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (perfilActualDTO == null) {
			response.put("mensaje", "Error: No se pudo actualizar, el perfil con el ID: ".concat(id.toString())
					.concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		UserDTO userEncontradoDTO = null;
		
		try {
			// RECUPEAR EL USER DESDE LA BASE DE DATOS			
			userEncontradoDTO = this.userService.findById(profileDTO.getUserId());
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (userEncontradoDTO == null) {
			response.put("mensaje", "Error: No se pudo actualizar el perfil para el usuario con el ID: ".concat(profileDTO.getUserId().toString())
					.concat(" porque no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			// TODO: VALIDAR QUE EL USUARIO NO ESTE YA ASOCIADO AL PERFIL.
			perfilActualDTO.setBio(profileDTO.getBio());
			perfilActualDTO.setWebsite(profileDTO.getWebsite());
			perfilActualDTO.setUserId(profileDTO.getUserId());
			// perfilActualDTO.setUser(userEncontrado);

			perfilActualizadoDTO = this.profileService.save(perfilActualDTO);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el Perfil en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "¡El Perfil ha sido actualizado con éxito!");
		response.put("perfil", perfilActualizadoDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/profiles/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		ProfileDTO perfilDTO = this.profileService.findById(id);
		
		if (perfilDTO == null) {
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
