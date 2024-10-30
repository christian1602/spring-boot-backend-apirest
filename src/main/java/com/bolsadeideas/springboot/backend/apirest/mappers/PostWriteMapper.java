package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PostEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostWriteDTO;

@Mapper(componentModel = "spring")
public interface PostWriteMapper {
	
	PostWriteMapper INSTANCE = Mappers.getMapper(PostWriteMapper.class);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", ignore = true)
	PostEntity toPostEntity(PostWriteDTO postWriteDTO);	
}
