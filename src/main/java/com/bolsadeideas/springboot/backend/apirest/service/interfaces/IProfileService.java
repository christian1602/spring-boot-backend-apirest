package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileDTO;

public interface IProfileService {

	List<ProfileDTO> findAll();
	ProfileDTO findById(Long id);
	ProfileDTO save(ProfileDTO profileDTO);
	void delete(Long id);
}
