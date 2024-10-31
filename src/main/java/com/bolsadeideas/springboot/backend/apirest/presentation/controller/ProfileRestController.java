package com.bolsadeideas.springboot.backend.apirest.presentation.controller;

import java.util.List;

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

import com.bolsadeideas.springboot.backend.apirest.exception.InvalidDataException;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.response.ApiResponseDTO;
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
	public List<ProfileReadDTO> index() {
		return this.profileService.findAll();
	}

	@GetMapping("/profiles/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		ProfileReadDTO perfilReadDTO = this.profileService.findById(id);
		return new ResponseEntity<ProfileReadDTO>(perfilReadDTO, HttpStatus.OK);
	}

	@PostMapping("/profiles")
	public ResponseEntity<?> create(@Valid @RequestBody ProfileWriteDTO profileWriteDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		
		ProfileReadDTO savedProfileReadDTO = this.profileService.save(profileWriteDTO);		
		ApiResponseDTO<ProfileReadDTO> response = new ApiResponseDTO<>("¡El perfil ha sido creado con éxito!",savedProfileReadDTO);
		
		return new ResponseEntity<ApiResponseDTO<ProfileReadDTO>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/profiles/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		this.profileService.delete(id);		
		
		ApiResponseDTO<ProfileReadDTO> response = new ApiResponseDTO<>("¡El Perfil ha sido eliminado con éxito!",null);

		return new ResponseEntity<ApiResponseDTO<ProfileReadDTO>>(response, HttpStatus.OK);
	}
}
