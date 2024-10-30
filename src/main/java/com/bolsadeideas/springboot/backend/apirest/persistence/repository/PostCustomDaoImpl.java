package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bolsadeideas.springboot.backend.apirest.presentation.dto.PostReadWithUserIdDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class PostCustomDaoImpl implements IPostCustomDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<PostReadWithUserIdDTO> findAllPostsWithUserId() {				
		// HIBERNATE NO TIENE COMPATIBILIDAD DIRECTA CON RECORDS EN LAS CONSULTAS JPQL PARA INSTANCIRALOS AUTOMATICAMENTE
		
		String jpql = "SELECT p.id, p.title, p.body, u.id FROM PostEntity p JOIN p.user u";
		
		List<Object[]> results = this.entityManager.createQuery(jpql,Object[].class).getResultList();
		
		return results.stream()
                 .map(obj -> new PostReadWithUserIdDTO((Long) obj[0], (String) obj[1], (String) obj[2], (Long) obj[3]))
                 .toList();
		
		// SI PostReadWithUserIdDTO FUERA UNA CLASE COMUN PODEMOS USAR ESTAS LINEAS
		// TypedQuery<PostReadWithUserIdDTO> query = this.entityManager.createQuery(jpql,PostReadWithUserIdDTO.class);
		// return query.getResultList();
	}
	
	/*
	public List<PostDTO> findAllPostsWithUserId() {
		String jpql = "SELECT new com.bolsadeideas.springboot.backend.apirest.dto.PostDTO(p.id, p.title, p.body, u.id) FROM Post p JOIN p.user u";
		TypedQuery<PostDTO> query = this.entityManager.createQuery(jpql, PostDTO.class);
		return query.getResultList();
	}
	*/
}
