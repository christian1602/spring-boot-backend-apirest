package com.bolsadeideas.springboot.backend.apirest.dto;

public class PostDto {

	private Long postId;
	private String title;
	private String body;
	private Long userId;

	public PostDto(Long postId, String title, String body, Long userId) {
		this.postId = postId;
		this.title = title;
		this.body = body;
		this.userId = userId;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
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
}
