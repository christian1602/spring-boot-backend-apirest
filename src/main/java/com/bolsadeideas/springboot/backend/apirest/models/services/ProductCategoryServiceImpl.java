package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IProductCategoryDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.ProductCategory;

@Service
public class ProductCategoryServiceImpl implements IProductCategoryService {

	private final IProductCategoryDao productCategoryDao;

	public ProductCategoryServiceImpl(IProductCategoryDao productCategoryDao) {
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
