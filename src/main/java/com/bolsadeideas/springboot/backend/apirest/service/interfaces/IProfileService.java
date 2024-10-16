package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProfileEntity;

public interface IProfileService {

	List<ProfileEntity> findAll();
	ProfileEntity findById(Long id);
	ProfileEntity save(ProfileEntity profile);
	void delete(Long id);
}
