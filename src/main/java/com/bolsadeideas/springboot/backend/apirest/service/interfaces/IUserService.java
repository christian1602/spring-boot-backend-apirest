package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UserDTO;

public interface IUserService {

	List<UserDTO> findAll();
	UserDTO findById(Long id);
	// UserDTO save(UserDTO userDTO);
	UserDTO update(Long id, UserDTO userDTO);
	void delete(Long id);
}
