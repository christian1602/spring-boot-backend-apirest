package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IProductDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Product;

@Service
public class ProductServiceImpl implements IProductService {

	private final IProductDao productoDao;

	public ProductServiceImpl(IProductDao productoDao) {
		this.productoDao = productoDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> findAll() {
		return (List<Product>) this.productoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Product findById(Long id) {
		return this.productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Product save(Product product) {
		return this.productoDao.save(product);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.productoDao.deleteById(id);
	}
}
