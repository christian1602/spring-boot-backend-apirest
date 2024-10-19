package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProfileDTO {

	private Long id;

	@NotBlank
	private String bio;

	@NotBlank
	private String website;

	@NotNull
	private Long userId;

	public ProfileDTO() {
	}

	public ProfileDTO(Long id, String bio, String website, Long userId) {
		this.id = id;
		this.bio = bio;
		this.website = website;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "ProfileDTO [id=" + id + ", bio=" + bio + ", website=" + website + ", userId=" + userId + "]";
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
		ProfileDTO other = (ProfileDTO) obj;
		return Objects.equals(id, other.id);
	}
}
