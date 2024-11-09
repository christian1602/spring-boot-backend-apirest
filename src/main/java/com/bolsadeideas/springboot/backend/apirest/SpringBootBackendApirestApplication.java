package com.bolsadeideas.springboot.backend.apirest;

import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PermissionEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.PostEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.RoleEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.RoleEnum;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IPostRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IUserRepository;

@SpringBootApplication
public class SpringBootBackendApirestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBackendApirestApplication.class, args);
	}

    @Bean
    CommandLineRunner init(IUserRepository userRepository, IPostRepository postRepository){
		return args -> {
			// CREATE PERMISSIONS
			PermissionEntity createPermission = new PermissionEntity();
			createPermission.setName("CREATE");
			PermissionEntity readPermission = new PermissionEntity();
			readPermission.setName("READ");
			PermissionEntity updatePermission = new PermissionEntity();
			updatePermission.setName("UPDATE");
			PermissionEntity deletePermission = new PermissionEntity();
			deletePermission.setName("DELETE");
			PermissionEntity refactorPermission = new PermissionEntity();
			refactorPermission.setName("REFACTOR");

			// CREATE ROLES
			RoleEntity adminRole = new RoleEntity();
			adminRole.setRoleEnum(RoleEnum.ADMIN);
			adminRole.setPermissions(Set.of(createPermission,readPermission,updatePermission,deletePermission));
			
			RoleEntity developerRole = new RoleEntity();
			developerRole.setRoleEnum(RoleEnum.DEVELOPER);
			developerRole.setPermissions(Set.of(createPermission,readPermission,updatePermission,deletePermission,refactorPermission));

			RoleEntity userRole = new RoleEntity();
			userRole.setRoleEnum(RoleEnum.USER);
			userRole.setPermissions(Set.of(createPermission));
			// userRole.setPermissions(Set.of(createPermission,readPermission));

			RoleEntity guestRole = new RoleEntity();
			guestRole.setRoleEnum(RoleEnum.GUEST);
			guestRole.setPermissions(Set.of(readPermission));

			// CREATE USERS
			UserEntity christianUser = new UserEntity();
			christianUser.setEmail("christian@mail.com");
			christianUser.setUsername("christian");
			christianUser.setPassword("$2a$10$qG3wS1Evr6WNJIJof5TEXOV5CTIJZDrenVSSxqJ2kIWbq6HDydVNi");
			christianUser.setEnabled(true);
			christianUser.setAccountNoExpired(true);
			christianUser.setAccountNoLocked(true);
			christianUser.setCredentialNoExpired(true);
			christianUser.setRoles(Set.of(adminRole));
			
			UserEntity bellaUser = new UserEntity();			
			bellaUser.setEmail("bella@mail.com");
			bellaUser.setUsername("bella");
			bellaUser.setPassword("$2a$10$qG3wS1Evr6WNJIJof5TEXOV5CTIJZDrenVSSxqJ2kIWbq6HDydVNi");
			bellaUser.setEnabled(true);
			bellaUser.setAccountNoExpired(true);
			bellaUser.setAccountNoLocked(true);
			bellaUser.setCredentialNoExpired(true);
			bellaUser.setRoles(Set.of(developerRole));
			
			UserEntity walterUser = new UserEntity();
			walterUser.setEmail("walter@mail.com");
			walterUser.setUsername("walter");
			walterUser.setPassword("$2a$10$qG3wS1Evr6WNJIJof5TEXOV5CTIJZDrenVSSxqJ2kIWbq6HDydVNi");
			walterUser.setEnabled(true);
			walterUser.setAccountNoExpired(true);
			walterUser.setAccountNoLocked(true);
			walterUser.setCredentialNoExpired(true);
			walterUser.setRoles(Set.of(userRole));
			
			UserEntity lizUser = new UserEntity();
			lizUser.setEmail("liz@mail.com");
			lizUser.setUsername("liz");
			lizUser.setPassword("$2a$10$qG3wS1Evr6WNJIJof5TEXOV5CTIJZDrenVSSxqJ2kIWbq6HDydVNi");
			lizUser.setEnabled(true);
			lizUser.setAccountNoExpired(true);
			lizUser.setAccountNoLocked(true);
			lizUser.setCredentialNoExpired(true);
			lizUser.setRoles(Set.of(guestRole));

			userRepository.saveAll(List.of(christianUser,walterUser,lizUser,bellaUser));			
		};
	}
}
