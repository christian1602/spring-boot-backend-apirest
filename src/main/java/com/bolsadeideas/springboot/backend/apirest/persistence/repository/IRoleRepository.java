package com.bolsadeideas.springboot.backend.apirest.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.RoleEntity;

public interface IRoleRepository extends CrudRepository<RoleEntity, Long> {

	// El metodo findRoleEntityByRoleEnumIn genera una consulta SQL que busca
    // todas las entidades RoleEntity cuyos valores en el campo roleEnum coinciden con cualquiera de los valores en la lista rolesName¿
    // SQL generado:
    // SELECT * FROM role_entity WHERE role_enum IN (?, ?, ..., ?);
    List<RoleEntity> findRoleEntityByRoleEnumIn(List<String> rolesName);
}
