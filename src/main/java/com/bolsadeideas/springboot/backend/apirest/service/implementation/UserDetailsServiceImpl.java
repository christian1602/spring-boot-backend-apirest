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
import com.bolsadeideas.springboot.backend.apirest.exception.UserNotFoundException;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.RoleEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.entity.UserEntity;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IRoleRepository;
import com.bolsadeideas.springboot.backend.apirest.persistence.repository.IUserRepository;
import com.bolsadeideas.springboot.backend.apirest.presentation.dto.UserWriteDTO;
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
	public AuthResponseDTO createUser(UserWriteDTO userWriteDTO) {
		// INICIO DE UN POSIBLE MAPPER PARA CONVERTIR UN CreateUserDTO A UN UserEntity
		// PERO DEBIDO A SU COMPLEJIDAD Y VALIDACIONES ADICIONALES, SE REALIZA MANUALMENTE 
		String email = userWriteDTO.email();
		String username = userWriteDTO.username();
		String password = userWriteDTO.password();
		List<String> roleRequest = userWriteDTO.authRolesDTO().roleListName();

		Set<RoleEntity> newRolesEntitySet = new HashSet<>(this.roleRepository.findRoleEntityByRoleEnumIn(roleRequest));

		if (newRolesEntitySet.isEmpty()) {
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
		userEntity.setRoles(newRolesEntitySet);
		// FIN DE UN POSIBLE MAPPER PARA CONVERTIR N CreateUserDTO A UN UserEntity

		UserEntity createdUserEntity = this.userRepository.save(userEntity);
		
		List<SimpleGrantedAuthority> authorityList = this.convertRolesToSimpleGrantedAuthorityList(createdUserEntity.getRoles());

		// CREAMOS UN Authentication TEMPORAL PARA CREAR TOKENS
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				createdUserEntity.getUsername(),
				createdUserEntity.getPassword(),
				authorityList);

		String accessToken = this.jwtUtils.createToken(authentication);
		String refreshToken = this.jwtUtils.createRefreshToken(authentication);
		
		return new AuthResponseDTO(createdUserEntity.getUsername(), "User created successfuly", accessToken, refreshToken, true);
	}
	
	@Override
	@Transactional
	public AuthResponseDTO updateUser(Long id, UserWriteDTO userWriteDTO) {
		 // BUSCAR EL USUARIO EXISTENTE EN LA BASE DE DATOS
		UserEntity existingUser = this.userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: ".concat(id.toString())));

		// ACTUALIZAR LOS CAMPOS SEGUN EL DTO RECIBIDO
		existingUser.setEmail(userWriteDTO.email());
	    existingUser.setUsername(userWriteDTO.username());
	    existingUser.setPassword(this.passwordEncoder.encode(userWriteDTO.password()));
	    
	    // OBTENER LOS NUEVOS REOLES DESDE EL DTO
	    List<String> roleRequest = userWriteDTO.authRolesDTO().roleListName();
	    Set<RoleEntity> newRolesEntitySet = new HashSet<>(this.roleRepository.findRoleEntityByRoleEnumIn(roleRequest));
	    
	    if (newRolesEntitySet.isEmpty()) {
	        throw new RolesSpecifiedNotExist("Roles specified does not exist for username: " + existingUser.getUsername());
	    }
	    
	    // DEBIDO A cascade = CascadeType.MERGE REALIZAMOS LAS SIGUIENTES VALIDACIONES
	    // MANEJO DE ROLES: ELIMINAR ROLES QUE NO ESTEN EN newRolesEntitySet
	    existingUser.getRoles().removeIf(role -> !newRolesEntitySet.contains(role)); // ELIMINAR ROLES NO DESEADOS
	    existingUser.getRoles().addAll(newRolesEntitySet); // AGREGAR NUEVOS ROLES QUE NO SE DUPLICARAN DEBIDO AL SET
	    
	    // GUARDAR EL USUARIO ACTUALIZADO
	    UserEntity updatedUserEntity = this.userRepository.save(existingUser);
	    
	    List<SimpleGrantedAuthority> authorityList = this.convertRolesToSimpleGrantedAuthorityList(updatedUserEntity.getRoles());
	    
	    // CREAMOS UN Authentication TEMPORAL PARA CREAR TOKENS 
	    Authentication authentication = new UsernamePasswordAuthenticationToken(
	    		updatedUserEntity.getUsername(),
	    		updatedUserEntity.getPassword(),
	            authorityList);
	    
	    String accessToken = this.jwtUtils.createToken(authentication);
	    String refreshToken = this.jwtUtils.createRefreshToken(authentication);
	    
	    return new AuthResponseDTO(updatedUserEntity.getUsername(), "User updated successfully", accessToken, refreshToken, true);
	}
	
	@Override
	public AuthResponseDTO loginUser(AuthLoginDTO authLoginDTO) {
		String username = authLoginDTO.username();
		String password = authLoginDTO.password();

		Authentication authentication = this.authenticate(username, password);
		// DEBIDO A QUE EN EL LOGIN TODAVIA NO EXISTE EL TOKEN,
		// EL FILTRO DE TOKEN NO SE CONSIDERA Y CONTINUA CON EL SIGUIENTE FILTRO
		// ENTONCES DEBEMOS USAR setAuthentication(authentication) PARA ESTABLECER LA NUEVA AUTENTICACION EN SPRING SECURITY
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String accessToken = this.jwtUtils.createToken(authentication);
		String refreshToken = this.jwtUtils.createRefreshToken(authentication);
		
		return new AuthResponseDTO(username, "User loged successfuly", accessToken, refreshToken, true);
	}
	
	@Override
	@Transactional(readOnly = true)
	public AuthResponseDTO refreshToken(RefreshTokenDTO refreshToken) {
		DecodedJWT decodedJWT = this.getDecodedJWT(refreshToken.refreshToken());
		
		if (!this.jwtUtils.isRefreshToken(decodedJWT)) {
			throw new InvalidTypeRefreshTokenException("Invalid type refresh token"); 
		}
		
		// CARGAMOS AL USUARIO DESDE EL TOKEN
        String username = this.jwtUtils.extractUsername(decodedJWT);
        UserEntity userEntity = this.userRepository.findUserEntityByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username: ".concat(username).concat(" not found")));
        
        // VERIFICAMOS SI EL TOKEN FUE EMITIDO ANTES DEL ULTIMO CAMBIO DE PASSWORD
        this.verifyIfTheTokenWasIssuedBeforeTheLastPasswordChange(userEntity,decodedJWT);
        
        // GENERAMOS UN NUEVO ACCESS TOKEN
        UserDetails userDetails = this.getUserDetails(username);
        // CREAMOS UN Authentication TEMPORAL PARA CREAR UN TOKEN
        Authentication authentication = new UsernamePasswordAuthenticationToken(
        		username,
        		userDetails.getPassword(),
        		userDetails.getAuthorities());        		
        String newAccessToken = this.jwtUtils.createToken(authentication);
        
		return new AuthResponseDTO(username, "Token refreshed successfully", newAccessToken, refreshToken.refreshToken(), true);
	}

	@Override
	@Transactional
	public AuthResponseDTO updatePassword(UpdatePasswordDTO updatePasswordDTO) {
		String username = updatePasswordDTO.username();
		String currentPassword = updatePasswordDTO.currentPassword();
		String newPassword = updatePasswordDTO.newPassword();
		
		UserDetails userDetails = this.getUserDetails(username);
		
        // VERIFICAR EL PASSWORD ACTUAL
		if (!this.isPasswordValid(currentPassword, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}
                
        UserEntity userEntityFound = this.userRepository.findUserEntityByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username: ".concat(username).concat(" not found")));
        
        // ACTUALIZAR EL PASSWORD
        userEntityFound.setPassword(this.passwordEncoder.encode(newPassword));
        userEntityFound.setLastPasswordChange(LocalDateTime.now()); // ACTUALIZA LA FECHA DE CAMBIO DE PASSWORD
        this.userRepository.save(userEntityFound);
        
        return new AuthResponseDTO(username, "Password updated successfully", null, null, true);
	}
	
	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = this.getUserDetails(username);
		
		if (!this.isPasswordValid(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}

		return new UsernamePasswordAuthenticationToken(
				username, 
				userDetails.getPassword(),
				userDetails.getAuthorities());
	}
	
	private UserDetails getUserDetails(String username) {
		UserDetails userDetails;
		
		try {
			userDetails = this.loadUserByUsername(username);
		} catch (UsernameNotFoundException ex){
			throw new BadCredentialsException("Invalid username");
		}
		
		return userDetails;
	}
	
	private DecodedJWT getDecodedJWT(String refreshToken) {
		DecodedJWT decodedJWT;
		
		try {
			decodedJWT = this.jwtUtils.validateToken(refreshToken);
		} catch(Exception e) {
			throw new InvalidRefreshTokenException("Invalid refresh token");
		}
		
		return decodedJWT;
	}
	
	private boolean isPasswordValid(String rawPassword, String encodedPassword) {
		return this.passwordEncoder.matches(rawPassword, encodedPassword);
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
	
	private void verifyIfTheTokenWasIssuedBeforeTheLastPasswordChange(UserEntity userEntity, DecodedJWT decodedJWT)
	{
        LocalDateTime lastPasswordChange = userEntity.getLastPasswordChange();
        Date tokenIssueDate = decodedJWT.getIssuedAt();
        LocalDateTime tokenIssueDateLocalDateTime = this.convertDateToLocalDateTime(tokenIssueDate);
        
        if (lastPasswordChange.isAfter(tokenIssueDateLocalDateTime)) {
        	throw new InvalidRefreshTokenException("Refresh token is no longer valid due to password change");
        }
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
