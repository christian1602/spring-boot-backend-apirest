package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.CategoryEntity;

public interface ICategoryRepository extends CrudRepository<CategoryEntity, Long> {

}
