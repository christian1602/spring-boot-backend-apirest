package com.bolsadeideas.springboot.backend.apirest.presentation.controllers;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.exceptions.InvalidDataException;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" })
public class ProfileRestController {

	private final IProfileService profileService;	

	public ProfileRestController(IProfileService profileService) {
		this.profileService = profileService;
	}

	@GetMapping("/profiles")
	public List<ProfileDTO> index() {
		return this.profileService.findAll();
	}

	@GetMapping("/profiles/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		ProfileDTO perfilDTO = this.profileService.findById(id);
		return new ResponseEntity<ProfileDTO>(perfilDTO, HttpStatus.OK);
	}

	@PostMapping("/profiles")
	public ResponseEntity<?> create(@Valid @RequestBody ProfileDTO profileDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}		
		
		ProfileDTO savedProfileDTO = this.profileService.save(profileDTO);		

		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El perfil ha sido creado con éxito!");
		response.put("perfil", savedProfileDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/profiles/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		this.profileService.delete(id);
		response.put("mensaje", "¡El Perfil ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
