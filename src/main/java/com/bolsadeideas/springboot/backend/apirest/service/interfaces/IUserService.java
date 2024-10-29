package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UserReadDTO;

public interface IUserService {

	List<UserReadDTO> findAll();
	UserReadDTO findById(Long id);
	// AHORA LO REALIZA IAuthUserService CON createUser
	// UserDTO save(UserDTO userDTO);
	// AHORA LO REALIZA IAuthUserService CON updateUser
	// UserEntity update(Long id, UserEntity userDTO);
	void delete(Long id);
}
