package com.br.alura.forum.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.br.alura.forum.domain.usuario.Usuario;

@Service
public class TokenService {
	
	// Parte 6 na criação de Autenticação de Usuário
	
	@Value("${forum.security.token.secret}")
	private String secret;
	
	private static final String ISSUER = "Fórum Alura";

	public String gerarToken(Usuario usuario) {
		try {
			var algoritmo = Algorithm.HMAC256(secret);
			return JWT.create()
					.withIssuer(ISSUER)
					.withSubject(usuario.getEmail())
					.withExpiresAt(dataExpiracao())
					.sign(algoritmo);
		} catch (JWTCreationException exception) {
			throw new RuntimeException("erro ao gerar token jwt: ", exception);
		}
	}
	
	// Parte 9 na criação de Autenticação de Usuário
	
	public String getSubject(String tokenJWT) {
		try {
			var algoritmo = Algorithm.HMAC256(secret);
			return JWT.require(algoritmo)
					.withIssuer(ISSUER)
					.build()
					.verify(tokenJWT)
					.getSubject();
		} catch (JWTVerificationException exception) {
			throw new RuntimeException("Token JWT inválido ou expirado!");
		}
	}

	private Instant dataExpiracao() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}

}
