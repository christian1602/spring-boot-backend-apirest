package com.bolsadeideas.springboot.backend.apirest.utils;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtils {

	private final String privateKey;
	private final String userGenerator;

	public JwtUtils(
			@Value("${security.jwt.key.private}") String privateKey,
			@Value("${security.jwt.user.generator}") String userGenerator) {
		this.privateKey = privateKey;
		this.userGenerator = userGenerator;
	}

	public String createToken(Authentication authentication) {
		Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

		String username = authentication.getPrincipal().toString();
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		return JWT.create()
				.withIssuer(this.userGenerator)
				.withSubject(username)
				.withClaim("authorities", authorities)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
				.withJWTId(UUID.randomUUID().toString())
				.withNotBefore(new Date(System.currentTimeMillis()))
				.sign(algorithm);
	}

	public DecodedJWT validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(this.userGenerator).build();
			// RETORNA EL TOKEN DECODIFICADO
			return verifier.verify(token);
		} catch (JWTVerificationException exception) {
			throw new JWTVerificationException("Invalid token, not Authorized");
		}
	}

	public String extractUsername(DecodedJWT decodedJWT) {
		return decodedJWT.getSubject();
	}

	public Claim getEspecificClaim(DecodedJWT decodedJWT, String nameClaim) {
		return decodedJWT.getClaim(nameClaim);
	}

	public Map<String, Claim> getAllClaims(DecodedJWT decodedJWT) {
		return decodedJWT.getClaims();
	}
}
