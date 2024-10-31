package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ClienteEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ClienteWriteDTO;

@Mapper(componentModel = "spring")
public interface ClienteWriteMapper {

	ClienteWriteMapper INSTANCE = Mappers.getMapper(ClienteWriteMapper.class);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	ClienteEntity toClienteEntityDTO(ClienteWriteDTO clienteWriteDTO);
}
