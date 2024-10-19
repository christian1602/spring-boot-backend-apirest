package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductDTO;

@Mapper(componentModel = "spring")
public interface ProductMapper {

	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	ProductDTO productEntityToProductDTO(ProductEntity productEntity);

	@Mapping(target = "productCategories", ignore = true)
	ProductEntity productDTOToProductEntity(ProductDTO productDTO);
}
