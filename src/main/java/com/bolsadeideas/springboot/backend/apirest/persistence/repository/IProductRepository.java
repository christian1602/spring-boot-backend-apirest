package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductEntity;

public interface IProductRepository extends JpaRepository<ProductEntity, Long> {

	boolean existsByProductCategoriesProductId(Long productId);
}
