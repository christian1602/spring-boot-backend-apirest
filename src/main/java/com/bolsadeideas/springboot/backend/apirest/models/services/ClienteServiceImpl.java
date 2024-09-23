package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IClienteDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService {
	
	@Autowired
	private IClienteDao clienteDao;
	
	// LA ANOTACION @Transactional PUEDE OMITIRSE YA QUE EL METODO findAll DE LA INTERFACE CrudRepository YA VIENE ANOTADA CON @Transactional
	// LA ANOTACION @Transactional ES ESTRICTAMETNE NECESARIA SI LA INTERFAZ IClienteDao TUVIERA NUEVOS METODOS POR IMPLEMENTAR
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) this.clienteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) { 
		return this.clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional()
	public Cliente save(Cliente cliente) {
		return this.clienteDao.save(cliente);
	}

	@Override
	@Transactional()
	public void delete(Long id) {
		this.clienteDao.deleteById(id);
	}
}
