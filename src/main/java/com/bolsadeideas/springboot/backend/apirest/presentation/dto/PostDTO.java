package com.bolsadeideas.springboot.backend.apirest.presentation.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PostDTO {

	private Long id;

	@NotBlank
	private String title;

	@NotBlank
	private String body;

	@NotNull
	private Long userId;

	public PostDTO() {

	}

	public PostDTO(Long id, String title, String body, Long userId) {
		this.id = id;
		this.title = title;
		this.body = body;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "PostDTO [id=" + id + ", title=" + title + ", body=" + body + ", userId=" + userId + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(body, id, title, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostDTO other = (PostDTO) obj;
		return Objects.equals(body, other.body) && Objects.equals(id, other.id) && Objects.equals(title, other.title)
				&& Objects.equals(userId, other.userId);
	}
}
