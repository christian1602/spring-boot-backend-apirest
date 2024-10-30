package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProfileEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileWriteDTO;

@Mapper(componentModel = "spring")
public interface ProfileWriteMapper {

	ProfileWriteMapper INSTANCE = Mappers.getMapper(ProfileWriteMapper.class);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", ignore = true)
	ProfileEntity toProfileEntity(ProfileWriteDTO profileWriteDTO);
}
