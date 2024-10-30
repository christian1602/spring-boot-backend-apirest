package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductCategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductCategoryReadDTO;

@Mapper(componentModel = "spring")
public interface ProductCategoryReadMapper {

	ProductCategoryReadMapper INSTANCE = Mappers.getMapper(ProductCategoryReadMapper.class);
		
	ProductCategoryReadDTO toProductCategoryReadDTO(ProductCategoryEntity productCategoryEntity);
}
