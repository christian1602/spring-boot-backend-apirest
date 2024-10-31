package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ClienteEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ClienteReadDTO;

@Mapper(componentModel = "spring")
public interface ClienteReadMapper {
	
	ClienteReadMapper INSTANCE = Mappers.getMapper(ClienteReadMapper.class);
	
	ClienteReadDTO toClienteReadDTO(ClienteEntity clienteEntity);
}
