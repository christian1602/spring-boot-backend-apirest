package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ClienteDTO;

public interface IClienteService {
	
	List<ClienteDTO> findAll();
	ClienteDTO findById(Long id);
	ClienteDTO save(ClienteDTO cliente);
	ClienteDTO update(Long id, ClienteDTO cliente);
	void delete(Long id);
}
