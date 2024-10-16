package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PostEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostDTO;

@Mapper(uses = {UserMapper.class})
public interface PostMapper {
	
	PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
	
	PostDTO postEntityTOPostDTO(PostEntity postEntity);
		
	PostEntity postDTOTOPostEntity(PostDTO postDTO);
}
