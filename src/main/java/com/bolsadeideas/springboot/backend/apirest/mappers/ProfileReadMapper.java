package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProfileEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileReadDTO;

@Mapper(componentModel = "spring")
public interface ProfileReadMapper {

	ProfileReadMapper INSTANCE = Mappers.getMapper(ProfileReadMapper.class);
	
	ProfileReadDTO ToProfileReadDTO(ProfileEntity profileEntity);
}
