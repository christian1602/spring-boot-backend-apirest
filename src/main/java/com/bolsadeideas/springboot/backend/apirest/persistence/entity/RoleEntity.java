package com.bolsadeideas.springboot.backend.apirest.persistence.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "roles")
public class RoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// SE ESTABLECE QUE LA COLUMNA role_name ES DE TIPO STRING EN LA BASE DE DATOS
	// SPRING SE ENCARGA DE MAPEAR EL ENUMERADO A STRING EN LA BASE DE DATOS
	@Enumerated(EnumType.STRING)
	@Column(name = "role_name")
	private RoleEnum roleEnum;

	@ManyToMany(
			targetEntity = PermissionEntity.class, 
			fetch = FetchType.EAGER, 
			cascade = CascadeType.ALL)
	@JoinTable(
			name = "role_permission", 
			joinColumns = @JoinColumn(name = "role_id"), 
			inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<PermissionEntity> permissions = new HashSet<>();

	public RoleEntity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleEnum getRoleEnum() {
		return roleEnum;
	}

	public void setRoleEnum(RoleEnum roleEnum) {
		this.roleEnum = roleEnum;
	}

	public Set<PermissionEntity> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<PermissionEntity> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "RoleEntity [id=" + id + ", roleEnum=" + roleEnum + "]";
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
		RoleEntity other = (RoleEntity) obj;
		return Objects.equals(id, other.id);
	}
}
