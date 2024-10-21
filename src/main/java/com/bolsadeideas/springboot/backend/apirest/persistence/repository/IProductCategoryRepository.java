package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductCategoryEntity;

public interface IProductCategoryRepository extends CrudRepository<ProductCategoryEntity, Long> {

	Optional<ProductCategoryEntity> findByProductIdAndCategoryId(Long productId, Long categoryId);
}
