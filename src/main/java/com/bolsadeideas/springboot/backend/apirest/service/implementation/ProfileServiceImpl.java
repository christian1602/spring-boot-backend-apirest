package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exception.ProfileNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.exception.UserAlreadyHasProfileException;
import com.bolsadeideas.springboot.backend.apirest.exception.UserNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.mappers.ProfileReadMapper;
import com.bolsadeideas.springboot.backend.apirest.mappers.ProfileWriteMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProfileEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IProfileRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IUserRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProfileService;

@Service
public class ProfileServiceImpl implements IProfileService {
	
	private final IProfileRepository profileRepository;
	private final IUserRepository userRepository;
	private final ProfileReadMapper profileReadMapper;
	private final ProfileWriteMapper profileWriteMapper;
	
	public ProfileServiceImpl(
			IProfileRepository profileRepository,
			IUserRepository userRepository, 
			ProfileReadMapper profileReadMapper, 
			ProfileWriteMapper profileWriteMapper) {
		this.profileRepository = profileRepository;
		this.userRepository = userRepository;
		this.profileReadMapper = profileReadMapper;
		this.profileWriteMapper = profileWriteMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProfileReadDTO> findAll() {
		Iterable<ProfileEntity> profiles = this.profileRepository.findAll();
		return StreamSupport.stream(profiles.spliterator(), false)
				.map(this.profileReadMapper::toProfileReadDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ProfileReadDTO findById(Long id) {
		ProfileEntity profileEntity = this.profileRepository.findById(id)
				.orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: ".concat(id.toString())));		 
		return this.profileReadMapper.toProfileReadDTO(profileEntity);
	}

	@Override
	@Transactional
	public ProfileReadDTO save(ProfileWriteDTO profileWriteDTO) {
		UserEntity userEntity = this.userRepository.findById(profileWriteDTO.userId())
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: ".concat(profileWriteDTO.userId().toString())));
		
		Optional<ProfileEntity> existingProfileEntityOptional = this.profileRepository.findByUserId(userEntity.getId());
		
		if (existingProfileEntityOptional.isPresent()) {
			throw new UserAlreadyHasProfileException("User with ID: ".concat(userEntity.getId().toString()).concat(" already has a profile"));
		} 
		
		ProfileEntity profileEntity = this.profileWriteMapper.toProfileEntity(profileWriteDTO);
		profileEntity.setUser(userEntity);
		ProfileEntity savedProfileEntity = this.profileRepository.save(profileEntity);
		
		return this.profileReadMapper.toProfileReadDTO(savedProfileEntity);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.profileRepository.findById(id)
				.orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: ".concat(id.toString())));
		this.profileRepository.deleteById(id);
	}
}
