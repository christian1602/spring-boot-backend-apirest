package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ClienteReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ClienteWriteDTO;

public interface IClienteService {
	
	List<ClienteReadDTO> findAll();
	ClienteReadDTO findById(Long id);
	ClienteReadDTO save(ClienteWriteDTO clienteWriteDTO);
	ClienteReadDTO update(Long id, ClienteWriteDTO clienteWriteDTO);
	void delete(Long id);
}
