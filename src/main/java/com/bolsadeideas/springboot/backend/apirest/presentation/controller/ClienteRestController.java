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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.exception.InvalidDataException;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ClienteReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ClienteWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.response.ApiResponseDTO;
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
	public List<ClienteReadDTO> index() {
		return this.clienteService.findAll();
	}

	@GetMapping("/clientes/{id}")
	// @ResponseStatus(HttpStatus.OK) // PUEDE OMITIRSE YA QUE POR DEFECTO SIEMPRE
	// DEVUELVE HttpStatus.OK
	public ResponseEntity<?> show(@PathVariable Long id) {
		ClienteReadDTO clienteReadDTO = this.clienteService.findById(id);
		return new ResponseEntity<>(clienteReadDTO, HttpStatus.OK);
	}

	@PostMapping("/clientes")
	public ResponseEntity<?> create(@Valid @RequestBody ClienteWriteDTO clienteWriteDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
		
		ClienteReadDTO newClienteReadDTO = this.clienteService.save(clienteWriteDTO);		
		ApiResponseDTO<ClienteReadDTO> response = new ApiResponseDTO<>("¡El cliente ha sido creado con éxito!",newClienteReadDTO);		

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ClienteWriteDTO clienteWriteDTO, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}
				
		ClienteReadDTO updatedCienteReadDTO = this.clienteService.update(id,clienteWriteDTO);
		ApiResponseDTO<ClienteReadDTO> response = new ApiResponseDTO<>("¡El cliente ha sido actualizado con éxito!",updatedCienteReadDTO);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		this.clienteService.delete(id);
		
		ApiResponseDTO<ClienteReadDTO> response = new ApiResponseDTO<>("¡El cliente ha sido eliminado con éxito!",null);		

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
