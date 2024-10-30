package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.CategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryReadDTO;

@Mapper(componentModel = "spring")
public interface CategoryReadMapper {

	CategoryReadMapper INSTANCE = Mappers.getMapper(CategoryReadMapper.class);
		
	CategoryReadDTO toCategoryReadDTO(CategoryEntity categoryEntity);	
}
