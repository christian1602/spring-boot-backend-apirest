package com.bolsadeideas.springboot.backend.apirest.persistence.entity;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "profiles")
public class ProfileEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "bio", nullable = false)
	private String bio;

	@Column(name = "website", nullable = false)
	private String website;

	@OneToOne(
			targetEntity = UserEntity.class, 
			cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	public ProfileEntity() {
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

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Profile [id=" + id + ", bio=" + bio + ", website=" + website + ", user=" + (user != null ? user.getUsername() : "null") + "]";
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
		ProfileEntity other = (ProfileEntity) obj;
		return Objects.equals(id, other.id);
	}
}
