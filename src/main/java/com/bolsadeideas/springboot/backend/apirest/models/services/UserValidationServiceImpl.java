package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.backend.apirest.models.entity.User;

@Service
public class UserValidationServiceImpl implements IUserValidationService {
	
	private final IUserService userService;

	public UserValidationServiceImpl(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public ResponseEntity<?> validateUser(User user, Map<String, Object> response) {		
		User userActual = null;
		Long idUserActual = 0L;
		
		try {
			idUserActual = user.getId();
			userActual = this.userService.findById(idUserActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (userActual == null) {
			response.put("mensaje",
					"El User con el ID: ".concat(idUserActual.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		// Si no hay problemas, no retornamos nada (null) para continuar con el flujo
		return null;
	}

}
