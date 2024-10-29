package com.bolsadeideas.springboot.backend.apirest.presentation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UserReadDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IUserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200 " })
public class UserRestController {

	private final IUserService userService;	

	public UserRestController(IUserService userService) {
		this.userService = userService;		
	}
	
	@GetMapping("/users")
	// @PreAuthorize("hasAuthority('READ')")
	public List<UserReadDTO> index() {
		return this.userService.findAll();
	}

	@GetMapping("/users/{id}")
	// @PreAuthorize("hasAuthority('CREATE')")
	public ResponseEntity<?> show(@PathVariable Long id) {
		UserReadDTO userReadDTO = this.userService.findById(id);
		return new ResponseEntity<UserReadDTO>(userReadDTO, HttpStatus.OK);
	}

	/*
	 *  METODO LLEVADO A AuthenticationRestControler COMO register
	@PostMapping("/users")
	public ResponseEntity<?> create(@Valid @RequestBody CreateUserDTO userDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		
		CreateUserDTO nuevoUserDTO = this.userService.save(userDTO);

		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El Usuario ha sido creado con éxito!");
		response.put("user", nuevoUserDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	*/
	
	/*
	 * METODO LLEVADO A AuthenticationRestControler	COMO update
	@PutMapping("/users/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CreateUserDTO userDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		CreateUserDTO userActualizadoDTO = this.userService.update(id,userDTO);

		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El User ha sido actualizado con éxito!");
		response.put("user", userActualizadoDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	 */
	@DeleteMapping("/users/{id}")
	// @PreAuthorize("hasAuthority('DELETE')")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		this.userService.delete(id);
		response.put("mensaje", "¡El Usuario ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
