package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Post;

public interface IUserValidationService {

	ResponseEntity<?> validateUser(Post post, Map<String,Object>response);
}
