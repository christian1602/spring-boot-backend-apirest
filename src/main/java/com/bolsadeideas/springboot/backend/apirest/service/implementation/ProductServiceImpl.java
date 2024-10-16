package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IProductRepository;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProductService;

@Service
public class ProductServiceImpl implements IProductService {

	private final IProductRepository productoDao;

	public ProductServiceImpl(IProductRepository productoDao) {
		this.productoDao = productoDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductEntity> findAll() {
		return (List<ProductEntity>) this.productoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public ProductEntity findById(Long id) {
		return this.productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public ProductEntity save(ProductEntity product) {
		return this.productoDao.save(product);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.productoDao.deleteById(id);
	}
}
