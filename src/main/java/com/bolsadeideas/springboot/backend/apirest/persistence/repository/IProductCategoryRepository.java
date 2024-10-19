package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductCategoryEntity;

public interface IProductCategoryRepository extends CrudRepository<ProductCategoryEntity, Long> {

}