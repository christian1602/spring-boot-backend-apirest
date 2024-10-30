package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileWriteDTO;

public interface IProfileService {

	List<ProfileReadDTO> findAll();
	ProfileReadDTO findById(Long id);
	ProfileReadDTO save(ProfileWriteDTO profileWriteDTO);
	void delete(Long id);
}
