package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exceptions.UserNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.mappers.UserMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IUserRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UserDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	private final IUserRepository userRepository;
	private final UserMapper userMapper;

	public UserServiceImpl(IUserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findAll() {
		Iterable<UserEntity> users = this.userRepository.findAll();
		return StreamSupport.stream(users.spliterator(), false)
				.map(this.userMapper::userEntityToUserDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		UserEntity userEntity = this.userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: ".concat(id.toString())));		
		return this.userMapper.userEntityToUserDTO(userEntity);
	}
	/*
	@Override
	@Transactional
	public CreateUserDTO save(CreateUserDTO userDTO) {
		UserEntity userEntity = this.userMapper.UserDTOToUserEntity(userDTO);
		UserEntity userEntitySaved = this.userRepository.save(userEntity);
		return this.userMapper.userEntityToUserDTO(userEntitySaved);
	}
	*/
	@Override
	public UserDTO update(Long id, UserDTO userDTO) {
		UserEntity userEntity = this.userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: ".concat(id.toString())));
		
		userEntity.setUsername(userDTO.username());
		userEntity.setEmail(userDTO.email());
		
		UserEntity updatedUserEntity = this.userRepository.save(userEntity);
		
		return this.userMapper.userEntityToUserDTO(updatedUserEntity);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: ".concat(id.toString())));
		this.userRepository.deleteById(id);
	}
}
