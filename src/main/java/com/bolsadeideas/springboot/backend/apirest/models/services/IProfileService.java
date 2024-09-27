package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Profile;

public interface IProfileService {

	List<Profile> findAll();
	Profile findById(Long id);
	Profile save(Profile profile);
	void delete(Long id);
}
