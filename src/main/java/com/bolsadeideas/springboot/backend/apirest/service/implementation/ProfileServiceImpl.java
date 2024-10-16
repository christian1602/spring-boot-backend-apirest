package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProfileEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IProfileRepository;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProfileService;

@Service
public class ProfileServiceImpl implements IProfileService {
	
	private final IProfileRepository profileDao;
	
	public ProfileServiceImpl(IProfileRepository profileDao) {
		this.profileDao = profileDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProfileEntity> findAll() {
		return (List<ProfileEntity>) this.profileDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public ProfileEntity findById(Long id) {
		return this.profileDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public ProfileEntity save(ProfileEntity profile) { 
		return this.profileDao.save(profile);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.profileDao.deleteById(id);
	}
}
