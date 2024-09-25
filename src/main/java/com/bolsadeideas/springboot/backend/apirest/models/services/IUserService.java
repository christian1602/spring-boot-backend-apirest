package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.models.entity.User;

public interface IUserService {

	List<User> findAll();
	User findById(Long id);
	User save(User post);
	void delete(Long id);
}
