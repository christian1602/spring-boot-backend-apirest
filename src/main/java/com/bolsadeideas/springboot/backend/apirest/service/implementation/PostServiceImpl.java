package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
				.map(this.postMapper::postEntityTOPostDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public PostDTO findById(Long id) {
		Optional<PostEntity> postEntityOptional = this.postRepository.findById(id); 
		return postEntityOptional.map(this.postMapper::postEntityTOPostDTO).orElse(null);
	}

	@Override
	@Transactional
	public PostDTO save(PostDTO postDTO) {
		PostEntity postEntity = this.postMapper.postDTOTOPostEntity(postDTO);
		
		Optional<UserEntity> userEntityOptional = this.userRepository.findById(postDTO.getUserId());
		
		if (!userEntityOptional.isPresent()) {
			return null;
		}
		
		postEntity.setUser(userEntityOptional.get());
		
		PostEntity postEntitySaved = this.postRepository.save(postEntity);
		return this.postMapper.postEntityTOPostDTO(postEntitySaved);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.postRepository.deleteById(id);
	}	
}
