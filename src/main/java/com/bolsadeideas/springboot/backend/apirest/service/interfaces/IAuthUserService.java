package com.bolsadeideas.springboot.backend.apirest.service.interfaces;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthLoginDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CreateUserDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.RefreshTokenDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UpdatePasswordDTO;

public interface IAuthUserService {
	
	AuthResponseDTO createUser(CreateUserDTO createUserDTO);
	AuthResponseDTO loginUser(AuthLoginDTO authLoginDTO);
	AuthResponseDTO refreshToken(RefreshTokenDTO refreshToken);
	AuthResponseDTO updatePassword(UpdatePasswordDTO updatePasswordDTO);
}
