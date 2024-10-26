package com.bolsadeideas.springboot.backend.apirest.service.implementation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bolsadeideas.springboot.backend.apirest.exception.InvalidRefreshTokenException;
import com.bolsadeideas.springboot.backend.apirest.exception.InvalidTypeRefreshTokenException;
import com.bolsadeideas.springboot.backend.apirest.exception.RolesSpecifiedNotExist;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.RoleEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IRoleRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IUserRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.CreateUserDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.RefreshTokenDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UpdatePasswordDTO;
import com.bolsadeideas.springboot.backend.apirest.service.interfaces.IAuthUserService;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthLoginDTO;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.AuthResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.utils.JwtUtils;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, IAuthUserService {

	private final IUserRepository userRepository;
	private final IRoleRepository roleRepository;	
	private final PasswordEncoder passwordEncoder;	
	private final JwtUtils jwtUtils;

	public UserDetailsServiceImpl(
			IUserRepository userRepository, 
			IRoleRepository roleRepository, 			
			PasswordEncoder passwordEncoder,			
			JwtUtils jwtUtils) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;		
		this.passwordEncoder = passwordEncoder;
		this.jwtUtils = jwtUtils;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntityFound = this.userRepository.findUserEntityByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username: ".concat(username).concat(" not found")));

		List<SimpleGrantedAuthority> authorityList = this.convertRolesToSimpleGrantedAuthorityList(userEntityFound.getRoles());

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
	@Transactional
	public AuthResponseDTO createUser(CreateUserDTO createUserDTO) {
		// INICIO DE UN POSIBLE MAPPER PARA CONVERTIR UN CreateUserDTO A UN UserEntity
		// PERO DEBIDO A SU COMPLEJIDAD Y VALIDACIONES ADICIONALES, SE REALIZA MANUALMENTE 
		String email = createUserDTO.email();
		String username = createUserDTO.username();
		String password = createUserDTO.password();		
		List<String> roleRequest = createUserDTO.authRolesDTO().roleListName();

		Set<RoleEntity> roleEntitySet = new HashSet<>(this.roleRepository.findRoleEntityByRoleEnumIn(roleRequest));

		if (roleEntitySet.isEmpty()) {
			throw new RolesSpecifiedNotExist("Roles specified does not exist to username: ".concat(username));
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
		// FIN DE UN POSIBLE MAPPER PARA CONVERTIR N CreateUserDTO A UN UserEntity

		UserEntity userEntityCreated = this.userRepository.save(userEntity);
		
		List<SimpleGrantedAuthority> authorityList = this.convertRolesToSimpleGrantedAuthorityList(userEntityCreated.getRoles());

		Authentication authentication = new UsernamePasswordAuthenticationToken(
				userEntityCreated.getUsername(),
				userEntityCreated.getPassword(),
				authorityList);

		String accessToken = this.jwtUtils.createToken(authentication);
		String refreshToken = this.jwtUtils.createRefreshToken(authentication);
		
		return new AuthResponseDTO(userEntityCreated.getUsername(), "User created successfuly", accessToken, refreshToken, true);
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
		String refreshToken = this.jwtUtils.createRefreshToken(authentication);
		
		return new AuthResponseDTO(username, "User loged successfuly", accessToken, refreshToken, true);
	}
	
	@Override
	@Transactional(readOnly = true)
	public AuthResponseDTO refreshToken(RefreshTokenDTO refreshToken) {
		DecodedJWT decodedJWT;
		
		try {
			decodedJWT = this.jwtUtils.validateToken(refreshToken.refreshToken());
		} catch(Exception e) {
			throw new InvalidRefreshTokenException("Invalid refresh token");
		}
		
		if (!this.jwtUtils.isRefreshToken(decodedJWT)) {
			throw new InvalidTypeRefreshTokenException("Invalid type refresh token"); 
		}
		
		 // CARGAMOS AL USUARIO DESDE EL TOKEN
        String username = this.jwtUtils.extractUsername(decodedJWT);
        
        UserDetails userDetails;
        
        try {
        	userDetails = this.loadUserByUsername(username);	
        } catch(UsernameNotFoundException ex){
			throw new BadCredentialsException("Invalid username");
		}
        
        UserEntity userEntity = this.userRepository.findUserEntityByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username: ".concat(username).concat(" not found")));
        
        // VERIFICAMOS SI EL TOKEN FUE EMITIDO ANTES DEL ULTIMO CAMBIO DE PASSWORD
        LocalDateTime lastPasswordChange = userEntity.getLastPasswordChange();
        Date tokenIssueDate = decodedJWT.getIssuedAt();
        LocalDateTime tokenIssueDateLocalDateTime = this.convertDateToLocalDateTime(tokenIssueDate);
        
        if (lastPasswordChange.isAfter(tokenIssueDateLocalDateTime)) {
        	throw new InvalidRefreshTokenException("Refresh token is no longer valid due to password change");
        }
        
		// GENERAMOS UN NUEVO ACCESS TOKEN
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),userDetails.getAuthorities());
        		
        String newAccessToken = this.jwtUtils.createToken(authentication);
        
		return new AuthResponseDTO(username, "Token refreshed successfully", newAccessToken, refreshToken.refreshToken(), true);
	}
	

	@Override
	@Transactional
	public AuthResponseDTO updatePassword(UpdatePasswordDTO updatePasswordDTO) {
		String username = updatePasswordDTO.username();
		String currentPassword = updatePasswordDTO.currentPassword();
		String newPassword = updatePasswordDTO.newPassword();
		
		// CARGAR EL USUARIO
		UserDetails userDetails;
        
        try {
        	userDetails = this.loadUserByUsername(username);
        } catch(UsernameNotFoundException ex){
			throw new BadCredentialsException("Invalid username");
		}
        
        // VERIFICAR EL PASSWORD ACTUAL
        if (!this.passwordEncoder.matches(currentPassword, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}
        
        // ACTUALIZAR EL PASSWORD
        UserEntity userEntityFound = this.userRepository.findUserEntityByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username: ".concat(username).concat(" not found")));
        
        userEntityFound.setPassword(this.passwordEncoder.encode(newPassword));
        userEntityFound.setLastPasswordChange(LocalDateTime.now()); // ACTUALIZA LA FECHA DE CAMBIO DE PASSWORD
        this.userRepository.save(userEntityFound);
        
        return new AuthResponseDTO(username, "Password updated successfully", null, null, true);
	}
	
	private Authentication authenticate(String username, String password) {
		
		UserDetails userDetails;
		
		try {
			userDetails = this.loadUserByUsername(username);
		} catch(UsernameNotFoundException ex){
			throw new BadCredentialsException("Invalid username");
		}		
		
		if (!this.passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}

		return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),userDetails.getAuthorities());
	}

	private List<SimpleGrantedAuthority> convertRolesToSimpleGrantedAuthorityList(Set<RoleEntity> roles) {
		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

		// EN SPRING SECURITY ES NECESARIO AGREGAR EL PREFIJO "ROLE_" PARA QUE UN ROL
		// SEA RECONOCIDO COMO TAL
		authorityList.addAll(roles.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())))
				.toList());

		// EN SPRING SECURITY NO ES NECESARIO AGREGAR NINGUN PREFIJO PARA QUE UN PERMISO
		// SEA RECONOCIDO COMO TAL
		authorityList.addAll(roles.stream()
				.flatMap(role -> role.getPermissions().stream()
						.map(permission -> new SimpleGrantedAuthority(permission.getName())))
				.toList());

		return authorityList;
	}
	
	// CONVERSION DE Date a LocalDateTime
	private LocalDateTime convertDateToLocalDateTime(Date date) {
	    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	
	/*
	private List<SimpleGrantedAuthority> convertRolesToSimpleGrantedAuthorityList_V2(Set<RoleEntity> roles) {
	    List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

	    // AGREGA LAS AUTORIDADES DE LOS ROLES
	    roles.forEach(role -> {
	        authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name()));
	        role.getPermissions().forEach(permission -> 
	            authorityList.add(new SimpleGrantedAuthority(permission.getName())));
	    });

	    return authorityList;
	}
	*/
}
