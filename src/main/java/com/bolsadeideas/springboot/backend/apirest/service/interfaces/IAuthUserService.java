package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthLoginDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CreateUserDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.RefreshTokenDTO;

public interface IAuthUserService {
	
	AuthResponseDTO createUser(CreateUserDTO authCreateUserDTO);
	AuthResponseDTO loginUser(AuthLoginDTO authLoginDTO);	
	AuthResponseDTO refreshToken(RefreshTokenDTO refreshToken);
}
