package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exception.ClienteNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.mappers.ClienteMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ClienteEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IClienteRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ClienteDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IClienteService;

@Service
public class ClienteServiceImpl implements IClienteService {
		
	private final IClienteRepository clienteRepository;
	private final ClienteMapper clienteMapper;
	
	public ClienteServiceImpl(IClienteRepository clienteRepository, ClienteMapper clienteMapper) { 
		this.clienteRepository = clienteRepository;
		this.clienteMapper = clienteMapper;
	}

	// LA ANOTACION @Transactional PUEDE OMITIRSE YA QUE EL METODO findAll DE LA INTERFACE CrudRepository YA VIENE ANOTADA CON @Transactional
	// LA ANOTACION @Transactional ES ESTRICTAMETNE NECESARIA SI LA INTERFAZ IClienteDao TUVIERA NUEVOS METODOS POR IMPLEMENTAR
	@Override
	@Transactional(readOnly = true)
	public List<ClienteDTO> findAll() {
		Iterable<ClienteEntity> clientes = this.clienteRepository.findAll();
		return StreamSupport.stream(clientes.spliterator(), false)
				.map(this.clienteMapper::clienteEntityToClienteDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ClienteDTO findById(Long id) {
		ClienteEntity clienteEntity = this.clienteRepository.findById(id)
				.orElseThrow(() -> new ClienteNotFoundException("Cliente not found with ID: ".concat(id.toString())));
		return this.clienteMapper.clienteEntityToClienteDTO(clienteEntity);
	}

	@Override
	@Transactional
	public ClienteDTO save(ClienteDTO clienteDTO) {
		ClienteEntity clienteEntity = this.clienteMapper.clienteDTOToClienteEntity(clienteDTO);
		ClienteEntity clienteEntitySaved = this.clienteRepository.save(clienteEntity); 
		return this.clienteMapper.clienteEntityToClienteDTO(clienteEntitySaved);
	}
	
	@Override
	@Transactional
	public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
		ClienteEntity clienteEntity = this.clienteRepository.findById(id)
				.orElseThrow(() -> new ClienteNotFoundException("Cliente not found with ID: ".concat(id.toString())));
		
		clienteEntity.setNombre(clienteDTO.nombre());
		clienteEntity.setApellido(clienteDTO.apellido());
		clienteEntity.setEmail(clienteDTO.email());
		
		ClienteEntity updatedClienteEntity = this.clienteRepository.save(clienteEntity); 
		
		return this.clienteMapper.clienteEntityToClienteDTO(updatedClienteEntity);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.clienteRepository.findById(id)
			.orElseThrow(() -> new ClienteNotFoundException("Cliente not found with ID: ".concat(id.toString())));
		this.clienteRepository.deleteById(id);
	}
}
