package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exception.PostNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.exception.UserNotCreatorException;
import com.bolsadeideas.springboot.backend.apirest.exception.UserNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.mappers.PostReadMapper;
import com.bolsadeideas.springboot.backend.apirest.mappers.PostWriteMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PostEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IPostRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IUserRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostWriteDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostService;

@Service
public class PostServiceImpl implements IPostService {

	private final IPostRepository postRepository;
	private final IUserRepository userRepository;
	private final PostReadMapper postReadMapper;
	private final PostWriteMapper postWriteMapper;

	public PostServiceImpl(
			IPostRepository postRepository, 
			IUserRepository userRepository, 
			PostReadMapper postReadMapper,
			PostWriteMapper postWriteMapper) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.postReadMapper = postReadMapper;
		this.postWriteMapper = postWriteMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PostReadDTO> findAll() {
		Iterable<PostEntity> posts =  this.postRepository.findAll();		
		return StreamSupport.stream(posts.spliterator(), false)
				.map(this.postReadMapper::toPostReadDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public PostReadDTO findById(Long id) {
		PostEntity postEntity = this.postRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException("Post not found with ID: ".concat(id.toString())));
		return this.postReadMapper.toPostReadDTO(postEntity);
	}

	@Override
	@Transactional
	public PostReadDTO save(PostWriteDTO postWriteDTO) {
		UserEntity userEntitY = this.userRepository.findById(postWriteDTO.userId())
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: ".concat(postWriteDTO.userId().toString())));
		
		PostEntity postEntity = this.postWriteMapper.toPostEntity(postWriteDTO);
		postEntity.setUser(userEntitY);
		PostEntity savedPostEntity = this.postRepository.save(postEntity);
		
		return this.postReadMapper.toPostReadDTO(savedPostEntity);
	}
	
	@Override
	@Transactional
	public PostReadDTO update(Long id, PostWriteDTO postWriteDTO) {
		PostEntity existingPostEntity = this.postRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException("Post not found with ID: ".concat(id.toString())));
		
	    if (!existingPostEntity.getUser().getId().equals(postWriteDTO.userId())) {
	        throw new UserNotCreatorException("User with ID: ".concat(postWriteDTO.userId().toString()).concat(" is not the creator of the Post"));
	    }
	    
	    UserEntity userEntity = this.userRepository.findById(postWriteDTO.userId())
				.orElseThrow(()-> new UserNotFoundException("User not found with ID: ".concat(postWriteDTO.userId().toString())));
		
		existingPostEntity.setTitle(postWriteDTO.title());
		existingPostEntity.setBody(postWriteDTO.body());		
		existingPostEntity.setUser(userEntity);
		
		PostEntity updatedPostEntity = this.postRepository.save(existingPostEntity);
		
		return this.postReadMapper.toPostReadDTO(updatedPostEntity);
	}	

	@Override
	@Transactional
	public void delete(Long id) {
		this.postRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException("Post not found with ID: ".concat(id.toString())));
		this.postRepository.deleteById(id);
	}
}
