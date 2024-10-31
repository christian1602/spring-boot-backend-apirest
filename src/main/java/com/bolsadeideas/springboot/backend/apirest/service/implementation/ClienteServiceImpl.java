package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exception.ClienteNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.mappers.ClienteReadMapper;
import com.bolsadeideas.springboot.backend.apirest.mappers.ClienteWriteMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ClienteEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IClienteRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ClienteReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ClienteWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IClienteService;

@Service
public class ClienteServiceImpl implements IClienteService {
		
	private final IClienteRepository clienteRepository;
	private final ClienteReadMapper clienteReadMapper;
	private final ClienteWriteMapper clienteWriteMapper;
	
	public ClienteServiceImpl(IClienteRepository clienteRepository, ClienteReadMapper clienteReadMapper, ClienteWriteMapper clienteWriteMapper) { 
		this.clienteRepository = clienteRepository;
		this.clienteReadMapper = clienteReadMapper;
		this.clienteWriteMapper = clienteWriteMapper;
	}

	// LA ANOTACION @Transactional PUEDE OMITIRSE YA QUE EL METODO findAll DE LA INTERFACE CrudRepository YA VIENE ANOTADA CON @Transactional
	// LA ANOTACION @Transactional ES ESTRICTAMETNE NECESARIA SI LA INTERFAZ IClienteDao TUVIERA NUEVOS METODOS POR IMPLEMENTAR
	@Override
	@Transactional(readOnly = true)
	public List<ClienteReadDTO> findAll() {
		Iterable<ClienteEntity> clientes = this.clienteRepository.findAll();
		return StreamSupport.stream(clientes.spliterator(), false)
				.map(this.clienteReadMapper::toClienteReadDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ClienteReadDTO findById(Long id) {
		ClienteEntity clienteEntity = this.clienteRepository.findById(id)
				.orElseThrow(() -> new ClienteNotFoundException("Cliente not found with ID: ".concat(id.toString())));
		return this.clienteReadMapper.toClienteReadDTO(clienteEntity);
	}

	@Override
	@Transactional
	public ClienteReadDTO save(ClienteWriteDTO clienteWriteDTO) {
		ClienteEntity clienteEntity = this.clienteWriteMapper.toClienteEntityDTO(clienteWriteDTO);
		ClienteEntity savedClienteEntity = this.clienteRepository.save(clienteEntity); 
		return this.clienteReadMapper.toClienteReadDTO(savedClienteEntity);
	}
	
	@Override
	@Transactional
	public ClienteReadDTO update(Long id, ClienteWriteDTO clienteWriteDTO) {
		ClienteEntity clienteEntity = this.clienteRepository.findById(id)
				.orElseThrow(() -> new ClienteNotFoundException("Cliente not found with ID: ".concat(id.toString())));
		
		clienteEntity.setNombre(clienteWriteDTO.nombre());
		clienteEntity.setApellido(clienteWriteDTO.apellido());
		clienteEntity.setEmail(clienteWriteDTO.email());
		
		ClienteEntity updatedClienteEntity = this.clienteRepository.save(clienteEntity); 
		
		return this.clienteReadMapper.toClienteReadDTO(updatedClienteEntity);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.clienteRepository.findById(id)
			.orElseThrow(() -> new ClienteNotFoundException("Cliente not found with ID: ".concat(id.toString())));
		this.clienteRepository.deleteById(id);
	}
}
