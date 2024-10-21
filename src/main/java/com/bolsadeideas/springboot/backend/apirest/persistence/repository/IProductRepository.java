package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductEntity;

public interface IProductRepository extends CrudRepository<ProductEntity, Long> {

	boolean existsByProductCategoriesProductId(Long productId);
}
