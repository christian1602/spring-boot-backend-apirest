package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProductCategory;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IProductCategoryRepository;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements IProductCategoryService {

	private final IProductCategoryRepository productCategoryDao;

	public ProductCategoryServiceImpl(IProductCategoryRepository productCategoryDao) {
		this.productCategoryDao = productCategoryDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductCategory> findAll() {
		return (List<ProductCategory>) this.productCategoryDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public ProductCategory findById(Long id) {
		return this.productCategoryDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public ProductCategory save(ProductCategory productCategory) {
		return this.productCategoryDao.save(productCategory);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.productCategoryDao.deleteById(id);
	}
}
