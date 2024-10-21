package com.bolsadeideas.springboot.backend.apirest.persistence.entity;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "product_category", uniqueConstraints = @UniqueConstraint(columnNames = {"product_id","category_id"}))
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProductCategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "description")
	private String description;

	@ManyToOne(targetEntity = ProductEntity.class)
	@JoinColumn(name = "product_id")
	@JsonBackReference(value = "productReference")
	private ProductEntity product;

	@ManyToOne(targetEntity = CategoryEntity.class)
	@JoinColumn(name = "category_id")
	@JsonBackReference(value = "categoryReference")
	private CategoryEntity category;

	public ProductCategoryEntity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "ProductCategory [id=" + id + ", description=" + description + ", product="
				+ (product != null ? product.getName() : "null") + ", category="
				+ (category != null ? category.getName() : "null") + "]";
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
		ProductCategoryEntity other = (ProductCategoryEntity) obj;
		return Objects.equals(id, other.id);
	}
}
