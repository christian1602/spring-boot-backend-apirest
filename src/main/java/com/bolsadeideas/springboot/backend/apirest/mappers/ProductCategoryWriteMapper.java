package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductCategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductCategoryWriteDTO;

@Mapper(componentModel = "spring")
public interface ProductCategoryWriteMapper {
	
	ProductCategoryWriteMapper INSTANCE = Mappers.getMapper(ProductCategoryWriteMapper.class);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "product", ignore = true)
	@Mapping(target = "category", ignore = true)
	ProductCategoryEntity toProductCategoryEntity(ProductCategoryWriteDTO productCategoryWriteDTO);
}
