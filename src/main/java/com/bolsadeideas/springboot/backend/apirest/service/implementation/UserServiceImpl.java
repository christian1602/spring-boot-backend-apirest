package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exception.UserNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.mappers.UserReadMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IUserRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UserReadDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	private final IUserRepository userRepository;
	private final UserReadMapper userReadMapper;

	public UserServiceImpl(IUserRepository userRepository, UserReadMapper userReadMapper) {
		this.userRepository = userRepository;
		this.userReadMapper = userReadMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserReadDTO> findAll() {
		Iterable<UserEntity> users = this.userRepository.findAll();
		return StreamSupport.stream(users.spliterator(), false)
				.map(this.userReadMapper::toUserReadDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public UserReadDTO findById(Long id) {
		UserEntity userEntity = this.userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: ".concat(id.toString())));		
		return this.userReadMapper.toUserReadDTO(userEntity);
	}
	/*
	@Override
	@Transactional
	public CreateUserDTO save(CreateUserDTO userDTO) {
		UserEntity userEntity = this.userMapper.UserDTOToUserEntity(userDTO);
		UserEntity userEntitySaved = this.userRepository.save(userEntity);
		return this.userMapper.userEntityToUserDTO(userEntitySaved);
	}
	
	@Override
	public UserEntity update(Long id, UserEntity userDTO) {
		UserEntity userEntity = this.userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: ".concat(id.toString())));
		
		userEntity.setUsername(userDTO.username());
		userEntity.setEmail(userDTO.email());
		
		UserEntity updatedUserEntity = this.userRepository.save(userEntity);
		
		return this.userWriterMapper.toUserEntity(updatedUserEntity);
	}
	 */
	@Override
	@Transactional
	public void delete(Long id) {
		this.userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: ".concat(id.toString())));
		this.userRepository.deleteById(id);
	}
}
