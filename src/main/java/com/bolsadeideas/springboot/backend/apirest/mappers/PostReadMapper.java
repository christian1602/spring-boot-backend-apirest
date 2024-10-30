package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PostEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadDTO;

@Mapper(componentModel = "spring")
public interface PostReadMapper {
	
	PostReadMapper INSTANCE = Mappers.getMapper(PostReadMapper.class);
			
	PostReadDTO toPostReadDTO(PostEntity postEntity);	
}
