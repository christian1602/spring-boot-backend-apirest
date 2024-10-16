package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ClienteEntity;

public interface IClienteService {
	
	List<ClienteEntity> findAll();
	ClienteEntity findById(Long id);
	ClienteEntity save(ClienteEntity cliente);
	void delete(Long id);
}
