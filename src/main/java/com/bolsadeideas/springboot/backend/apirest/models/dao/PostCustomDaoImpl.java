package com.bolsadeideas.springboot.backend.apirest.models.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bolsadeideas.springboot.backend.apirest.dto.PostDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class PostCustomDaoImpl implements IPostCustomDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<PostDto> findAllPostsWithUserId() {
		String jpql = "SELECT new com.bolsadeideas.springboot.backend.apirest.dto.PostDto(p.id, p.title, p.body, u.id) FROM Post p JOIN p.user u";
		TypedQuery<PostDto> query = this.entityManager.createQuery(jpql, PostDto.class);
		return query.getResultList();
	}
}
