package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class PostCustomDaoImpl implements IPostCustomDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<PostDTO> findAllPostsWithUserId() {
		String jpql = "SELECT new com.bolsadeideas.springboot.backend.apirest.dto.PostDto(p.id, p.title, p.body, u.id) FROM Post p JOIN p.user u";
		TypedQuery<PostDTO> query = this.entityManager.createQuery(jpql, PostDTO.class);
		return query.getResultList();
	}
}
