package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ClienteEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IClienteRepository;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IClienteService;

@Service
public class ClienteServiceImpl implements IClienteService {
	
	@Autowired
	private IClienteRepository clienteDao;
	
	// LA ANOTACION @Transactional PUEDE OMITIRSE YA QUE EL METODO findAll DE LA INTERFACE CrudRepository YA VIENE ANOTADA CON @Transactional
	// LA ANOTACION @Transactional ES ESTRICTAMETNE NECESARIA SI LA INTERFAZ IClienteDao TUVIERA NUEVOS METODOS POR IMPLEMENTAR
	@Override
	@Transactional(readOnly = true)
	public List<ClienteEntity> findAll() {
		return (List<ClienteEntity>) this.clienteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public ClienteEntity findById(Long id) { 
		return this.clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional()
	public ClienteEntity save(ClienteEntity cliente) {
		return this.clienteDao.save(cliente);
	}

	@Override
	@Transactional()
	public void delete(Long id) {
		this.clienteDao.deleteById(id);
	}
}
