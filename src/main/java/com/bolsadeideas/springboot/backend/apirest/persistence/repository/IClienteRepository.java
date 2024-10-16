package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ClienteEntity;

public interface IClienteRepository extends CrudRepository<ClienteEntity,Long> {

}
