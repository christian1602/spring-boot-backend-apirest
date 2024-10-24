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
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ClienteDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" })
public class ClienteRestController {

	private final IClienteService clienteService;

	public ClienteRestController(IClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@GetMapping("/clientes")
	public List<ClienteDTO> index() {
		return this.clienteService.findAll();
	}

	@GetMapping("/clientes/{id}")
	// @ResponseStatus(HttpStatus.OK) // PUEDE OMITIRSE YA QUE POR DEFECTO SIEMPRE
	// DEVUELVE HttpStatus.OK
	public ResponseEntity<?> show(@PathVariable Long id) {
		ClienteDTO clienteDTO = this.clienteService.findById(id);
		return new ResponseEntity<ClienteDTO>(clienteDTO, HttpStatus.OK);
	}

	@PostMapping("/clientes")
	public ResponseEntity<?> create(@Valid @RequestBody ClienteDTO clienteDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		
		ClienteDTO nuevoClienteDTO = this.clienteService.save(clienteDTO);
		Map<String, Object> response = new HashMap<>();		

		response.put("mensaje", "¡El cliente ha sido creado con éxito!");
		response.put("cliente", nuevoClienteDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
				
		ClienteDTO updatedCienteDTO = this.clienteService.update(id,clienteDTO);

		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El cliente ha sido actualizado con éxito!");
		response.put("cliente", updatedCienteDTO);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		this.clienteService.delete(id);
		
		Map<String, Object> response = new HashMap<>();
		response.put("mensaje", "¡El cliente ha sido eliminado con éxito!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
