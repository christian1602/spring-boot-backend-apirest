package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IUserDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.User;

@Service
public class UserServiceImpl implements IUserService {

	private final IUserDao userDao;

	public UserServiceImpl(IUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return (List<User>) this.userDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public User findById(Long id) {
		return this.userDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public User save(User user) {
		return this.userDao.save(user);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.userDao.deleteById(id);
	}
}
