package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exceptions.ProfileNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.exceptions.UserAlreadyHasProfileException;
import com.bolsadeideas.springboot.backend.apirest.exceptions.UserNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.mappers.ProfileMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.ProfileEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IProfileRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IUserRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.ProfileDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IProfileService;

@Service
public class ProfileServiceImpl implements IProfileService {
	
	private final IProfileRepository profileRepository;
	private final IUserRepository userRepository;
	private final ProfileMapper profileMapper;
	
	public ProfileServiceImpl(IProfileRepository profileRepository, IUserRepository userRepository, ProfileMapper profileMapper) {
		this.profileRepository = profileRepository;
		this.userRepository = userRepository;
		this.profileMapper = profileMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProfileDTO> findAll() {
		Iterable<ProfileEntity> profiles = this.profileRepository.findAll();
		return StreamSupport.stream(profiles.spliterator(), false)
				.map(this.profileMapper::ProfileEntityToProfileDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ProfileDTO findById(Long id) {
		ProfileEntity profileEntity = this.profileRepository.findById(id)
				.orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: ".concat(id.toString())));		 
		return this.profileMapper.ProfileEntityToProfileDTO(profileEntity);
	}

	@Override
	@Transactional
	public ProfileDTO save(ProfileDTO profileDTO) {
		UserEntity userEntity = this.userRepository.findById(profileDTO.getUserId())
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: ".concat(profileDTO.getUserId().toString())));
		
		Optional<ProfileEntity> existingProfileEntityOptional = this.profileRepository.findByUserId(userEntity.getId());
		
		if (existingProfileEntityOptional.isPresent()) {
			throw new UserAlreadyHasProfileException("User with ID: ".concat(userEntity.getId().toString()).concat(" already has a profile"));
		} 
		
		ProfileEntity profileEntity = this.profileMapper.profileDTOToProfileEntity(profileDTO);
		profileEntity.setUser(userEntity);
		ProfileEntity profileEntitySaved = this.profileRepository.save(profileEntity);
		
		return this.profileMapper.ProfileEntityToProfileDTO(profileEntitySaved);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.profileRepository.findById(id)
				.orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: ".concat(id.toString())));
		this.profileRepository.deleteById(id);
	}
}
