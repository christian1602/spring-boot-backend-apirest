package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		Optional<ProfileEntity> profileEntityOptional = this.profileRepository.findById(id); 
		return profileEntityOptional.map(this.profileMapper::ProfileEntityToProfileDTO).orElse(null);
	}

	@Override
	@Transactional
	public ProfileDTO save(ProfileDTO profileDTO) { 
		ProfileEntity profileEntity = this.profileMapper.profileDTOToProfileEntity(profileDTO);
		
		Optional<UserEntity> userEntityOptional = this.userRepository.findById(profileDTO.getUserId());
		
		if (userEntityOptional.isEmpty()) {
			return null;
		}
		
		profileEntity.setUser(userEntityOptional.get());
		
		ProfileEntity profileEntitySaved = this.profileRepository.save(profileEntity); 
		return this.profileMapper.ProfileEntityToProfileDTO(profileEntitySaved);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.profileRepository.deleteById(id);
	}
}
