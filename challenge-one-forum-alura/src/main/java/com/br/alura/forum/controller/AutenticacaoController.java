package com.br.alura.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.alura.forum.domain.usuario.DadosAutenticacao;
import com.br.alura.forum.domain.usuario.Usuario;
import com.br.alura.forum.infra.security.DadosTokenJWT;
import com.br.alura.forum.infra.security.TokenService;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private TokenService tokenService;
	
	@ApiOperation("Fazer login")
	@PostMapping
	public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
		var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
		var authentication = manager.authenticate(authenticationToken);
		
		// Parte 7 na criação de Autenticação de Usuário
		
		var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
		
		return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
		
	}
	
}


// Parte 3 na criação de Autenticação de Usuário