package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProfileEntity;

public interface IProfileRepository extends CrudRepository<ProfileEntity, Long> {

	Optional<ProfileEntity> findByUserId(Long userId);
}
