package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductWriteDTO;

@Mapper(componentModel = "spring")
public interface ProductWriteMapper {

	ProductWriteMapper INSTANCE = Mappers.getMapper(ProductWriteMapper.class);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "productCategories", ignore = true)
	ProductEntity toProductEntity(ProductWriteDTO productWriteDTO);
}
