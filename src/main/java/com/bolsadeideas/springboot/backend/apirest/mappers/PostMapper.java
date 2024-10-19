package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PostEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostDTO;

@Mapper(componentModel = "spring")
public interface PostMapper {
	
	PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
	
	@Mapping(source = "user.id", target = "userId")
	PostDTO postEntityToPostDTO(PostEntity postEntity);
		
	@Mapping(target = "user", ignore = true)
	PostEntity postDTOToPostEntity(PostDTO postDTO);
}
