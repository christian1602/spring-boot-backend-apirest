package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.exceptions.PostNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.exceptions.UserNotCreatorException;
import com.bolsadeideas.springboot.backend.apirest.exceptions.UserNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.mappers.PostMapper;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PostEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IPostRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IUserRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IPostService;

@Service
public class PostServiceImpl implements IPostService {

	private final IPostRepository postRepository;
	private final IUserRepository userRepository;
	private final PostMapper postMapper;

	public PostServiceImpl(IPostRepository postRepository, IUserRepository userRepository, PostMapper postMapper) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.postMapper = postMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PostDTO> findAll() {
		Iterable<PostEntity> posts =  this.postRepository.findAll();		
		return StreamSupport.stream(posts.spliterator(), false)
				.map(this.postMapper::postEntityToPostDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public PostDTO findById(Long id) {
		PostEntity postEntity = this.postRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException("Post not found with ID: ".concat(id.toString())));
		return this.postMapper.postEntityToPostDTO(postEntity);
	}

	@Override
	@Transactional
	public PostDTO save(PostDTO postDTO) {
		UserEntity userEntitY = this.userRepository.findById(postDTO.getUserId())
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: ".concat(postDTO.getUserId().toString())));
		
		PostEntity postEntity = this.postMapper.postDTOToPostEntity(postDTO);
		postEntity.setUser(userEntitY);
		PostEntity postEntitySaved = this.postRepository.save(postEntity);
		
		return this.postMapper.postEntityToPostDTO(postEntitySaved);
	}
	
	@Override
	@Transactional
	public PostDTO update(Long id, PostDTO postDTO) {
		PostEntity existingPostEntity = this.postRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException("Post not found with ID: ".concat(id.toString())));
		
	    if (!existingPostEntity.getUser().getId().equals(postDTO.getUserId())) {
	        throw new UserNotCreatorException("User with ID: ".concat(postDTO.getUserId().toString()).concat(" is not the creator of the Post"));
	    }
	    
	    UserEntity userEntity = this.userRepository.findById(postDTO.getUserId())
				.orElseThrow(()-> new UserNotFoundException("User not found with ID: ".concat(postDTO.getUserId().toString())));
		
		existingPostEntity.setTitle(postDTO.getTitle());
		existingPostEntity.setBody(postDTO.getBody());		
		existingPostEntity.setUser(userEntity);
		
		PostEntity updatedPostEntity = this.postRepository.save(existingPostEntity);
		
		return this.postMapper.postEntityToPostDTO(updatedPostEntity);
	}	

	@Override
	@Transactional
	public void delete(Long id) {
		this.postRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException("Post not found with ID: ".concat(id.toString())));
		this.postRepository.deleteById(id);
	}
}
