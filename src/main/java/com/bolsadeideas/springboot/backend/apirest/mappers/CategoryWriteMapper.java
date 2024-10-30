package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.CategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CategoryWriteDTO;

@Mapper(componentModel = "spring")
public interface CategoryWriteMapper {

	CategoryWriteMapper INSTANCE = Mappers.getMapper(CategoryWriteMapper.class);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "productCategories", ignore = true)
	CategoryEntity toCategoryEntity(CategoryWriteDTO categoryWriteDTO);
}
