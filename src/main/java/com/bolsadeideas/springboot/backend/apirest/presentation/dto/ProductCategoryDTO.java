package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductCategoryDTO {

	private Long id;

	@NotBlank
	private String description;

	@NotNull
	private Long productId;

	@NotNull
	private Long categoryId;

	public ProductCategoryDTO() {
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "ProductCategoryDTO [id=" + id + ", description=" + description + ", productId=" + productId
				+ ", categoryId=" + categoryId + "]";
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
		ProductCategoryDTO other = (ProductCategoryDTO) obj;
		return Objects.equals(id, other.id);
	}
}
