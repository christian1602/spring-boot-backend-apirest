package com.bolsadeideas.springboot.backend.apirest.persistence.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;

	@OneToMany(
			targetEntity = ProductCategoryEntity.class, 
			fetch = FetchType.LAZY, 
			mappedBy = "product")
	@JsonBackReference(value = "productReference")
	private Set<ProductCategoryEntity> productCategories = new HashSet<>();

	public ProductEntity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ProductCategoryEntity> getProductCategories() {
		return productCategories;
	}

	public void setProductCategories(Set<ProductCategoryEntity> productCategories) {
		this.productCategories = productCategories;
	}

	@Override
	public String toString() {
		return "ProductEntity [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductEntity other = (ProductEntity) obj;
		return Objects.equals(id, other.id);
	}
}
