package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IProfileDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Profile;

@Service
public class ProfileServiceImpl implements IProfileService {
	
	private final IProfileDao profileDao;
	
	public ProfileServiceImpl(IProfileDao profileDao) {
		this.profileDao = profileDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Profile> findAll() {
		return (List<Profile>) this.profileDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Profile findById(Long id) {
		return this.profileDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Profile save(Profile profile) { 
		return this.profileDao.save(profile);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.profileDao.deleteById(id);
	}
}
