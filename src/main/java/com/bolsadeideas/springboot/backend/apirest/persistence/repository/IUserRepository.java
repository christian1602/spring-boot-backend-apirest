package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;

public interface IUserRepository extends CrudRepository<UserEntity, Long> {
	 
	Optional<UserEntity> findUserEntityByUsername(String username);
}
