package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductReadDTO;

@Mapper(componentModel = "spring")
public interface ProductReadMapper {

	ProductReadMapper INSTANCE = Mappers.getMapper(ProductReadMapper.class);

	ProductReadDTO toProductReadDTO(ProductEntity productEntity);
}
