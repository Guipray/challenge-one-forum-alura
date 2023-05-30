package com.br.alura.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.alura.forum.domain.usuario.DadosDetalhamentoUsuario;
import com.br.alura.forum.domain.usuario.DadosUsuario;
import com.br.alura.forum.domain.usuario.Usuario;
import com.br.alura.forum.domain.usuario.UsuarioRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("login")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository repository;

	@PostMapping
	@Transactional
	public ResponseEntity cadastrar(@RequestBody @Valid DadosUsuario dados, UriComponentsBuilder uriBuilder) {
		var usuario = new Usuario(dados);
		repository.save(usuario);
		
		var uri = uriBuilder.path("/login/{id}").buildAndExpand(usuario.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
	}
	
}
