package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.CategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryDTO;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
		
	CategoryDTO categoryEntityToCategoryDTO(CategoryEntity categoryEntity);
	
	@Mapping(target = "productCategories", ignore = true)
	CategoryEntity categoryDTOToCategoryEntity(CategoryDTO categoryDTO);
}
