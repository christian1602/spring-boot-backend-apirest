package com.bolsadeideas.springboot.backend.apirest.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.RoleEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthRolesDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	@Mapping(source = "roles", target = "authRolesDTO")
	UserDTO userEntityToUserDTO(UserEntity userEntity);
	
    default AuthRolesDTO mapRolesToAuthRoleDTO(Set<RoleEntity> roles) {
        List<String> roleNames = roles.stream()
            .map(role -> "ROLE_".concat(role.getRoleEnum().name()))
            .collect(Collectors.toList());
        return new AuthRolesDTO(roleNames);
    }
}
