package com.bolsadeideas.springboot.backend.apirest.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductCategoryEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProductCategoryDTO;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

	ProductCategoryMapper INSTANCE = Mappers.getMapper(ProductCategoryMapper.class);
	
	@Mapping(source = "product.id", target = "productId")
	@Mapping(source = "category.id", target = "categoryId")
	ProductCategoryDTO productCategoryEntityToProductCategoryDTO(ProductCategoryEntity productCategoryEntity);
	
	@Mapping(target = "product", ignore = true)
	@Mapping(target = "category", ignore = true)
	ProductCategoryEntity productCategoryDTOToProductCategoryEntity(ProductCategoryDTO productCategoryDTO);
}
