package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	UserDTO userEntityToUserDTO(UserEntity userEntity);
	
	@Mapping(target = "posts", ignore = true)
	UserEntity UserDTOToUserEntity(UserDTO userDTO);
}
