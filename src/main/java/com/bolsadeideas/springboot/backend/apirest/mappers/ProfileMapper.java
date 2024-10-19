package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProfileEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileDTO;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

	ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

	@Mapping(source = "user.id", target = "userId")
	ProfileDTO ProfileEntityToProfileDTO(ProfileEntity profileEntity);

	@Mapping(target = "user", ignore = true)
	ProfileEntity profileDTOToProfileEntity(ProfileDTO profileDTO);
}
