package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.springboot.backend.apirest.models.entity.User;

public interface IUserValidationService {

	ResponseEntity<?> validateUser(User user, Map<String,Object>response);
}
