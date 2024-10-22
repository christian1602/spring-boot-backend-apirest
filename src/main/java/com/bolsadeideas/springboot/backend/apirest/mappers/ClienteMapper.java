package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ClienteEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ClienteDTO;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
	
	ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);
	
	ClienteDTO clienteEntityToClienteDTO(ClienteEntity clienteEntity);
	
	@Mapping(target = "createdAt", ignore = true)
	ClienteEntity clienteDTOToClienteEntity(ClienteDTO clienteDTO);
}
