package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.backend.apirest.persistence.entity.RoleEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IRoleRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IUserRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CreateUserDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IAuthUserService;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthLoginDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.utils.JwtUtils;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, IAuthUserService {

	private final IUserRepository userRepository;
	private final IRoleRepository roleRepository;
	private final JwtUtils jwtUtils;
	private final PasswordEncoder passwordEncoder;

	public UserDetailsServiceImpl(IUserRepository userRepository, IRoleRepository roleRepository, JwtUtils jwtUtils,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.jwtUtils = jwtUtils;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntityFound = this.userRepository.findUserEntityByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe"));

		List<SimpleGrantedAuthority> authorityList = this
				.convertRolesToSimpleGrantedAuthorityList(userEntityFound.getRoles());

		return new User(
				userEntityFound.getUsername(),
				userEntityFound.getPassword(), 
				userEntityFound.isEnabled(),
				userEntityFound.isAccountNoExpired(), 
				userEntityFound.isCredentialNoExpired(),
				userEntityFound.isAccountNoLocked(),
				authorityList);
	}

	@Override
	public AuthResponseDTO createUser(CreateUserDTO authCreateUserDTO) {
		String email = authCreateUserDTO.email();
		String username = authCreateUserDTO.username();
		String password = authCreateUserDTO.password();		
		List<String> roleRequest = authCreateUserDTO.authRolesDTO().roleListName();

		Set<RoleEntity> roleEntitySet = new HashSet<>(this.roleRepository.findRoleEntityByRoleEnumIn(roleRequest));

		if (roleEntitySet.isEmpty()) {
			throw new IllegalArgumentException("The roles specified does not exist");
		}
		
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(email);
		userEntity.setUsername(username);
		userEntity.setPassword(this.passwordEncoder.encode(password));
		userEntity.setEnabled(true);
		userEntity.setAccountNoLocked(true);
		userEntity.setAccountNoExpired(true);
		userEntity.setCredentialNoExpired(true);
		userEntity.setRoles(roleEntitySet);		

		UserEntity userCreated = this.userRepository.save(userEntity);
		List<SimpleGrantedAuthority> authorityList = this.convertRolesToSimpleGrantedAuthorityList(userCreated.getRoles());

		Authentication authentication = new UsernamePasswordAuthenticationToken(
				userCreated.getUsername(),
				userCreated.getPassword(),
				authorityList);

		String accessToken = this.jwtUtils.createToken(authentication);
		return new AuthResponseDTO(userCreated.getUsername(), "User created successfuly", accessToken, true);
	}
	
	@Override
	public AuthResponseDTO loginUser(AuthLoginDTO authLoginDTO) {
		String username = authLoginDTO.username();
		String password = authLoginDTO.password();

		Authentication authentication = this.authenticate(username, password);
		// DEBIDO A QUE EN EL LOGIN TODAVIA NO EXISTE EL TOKEN,
		// EL FILTRO DE TOKEN NO SE CONSIDERA Y CONTINUA CON EL SIGUIENTE FILTRO
		// ENTONCES DEBEMOS USAR setAuthentication(authentication) PARA ESTABLECER LA
		// NUEVA AUTENTICACION
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String accessToken = this.jwtUtils.createToken(authentication);
		return new AuthResponseDTO(username, "User loged successfuly", accessToken, true);
	}
	
	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = this.loadUserByUsername(username);

		if (userDetails == null) {
			throw new BadCredentialsException("Invalid username");
		}

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}

		return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),
				userDetails.getAuthorities());
	}

	private List<SimpleGrantedAuthority> convertRolesToSimpleGrantedAuthorityList(Set<RoleEntity> roles) {
		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

		// EN SPRING SECURITY ES NECESARIO AGREGAR EL PREFIJO "ROLE_" PARA QUE UN ROL
		// SEA RECONOCIDO COMO TAL
		authorityList.addAll(roles.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))).toList());

		// EN SPRING SECURITY NO ES NECESARIO AGREGAR NINGUN PREFIJO PARA QUE UN PERMISO
		// SEA RECONOCIDO COMO TAL
		authorityList.addAll(roles.stream().flatMap(role -> role.getPermissions().stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getName()))).toList());

		return authorityList;
	}
}
