package com.br.alura.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.alura.forum.domain.topico.DadosAtualizacaoTopico;
import com.br.alura.forum.domain.usuario.DadosAtualizacaoUsuario;
import com.br.alura.forum.domain.usuario.DadosDetalhamentoUsuario;
import com.br.alura.forum.domain.usuario.DadosListagemUsuario;
import com.br.alura.forum.domain.usuario.DadosUsuario;
import com.br.alura.forum.domain.usuario.Usuario;
import com.br.alura.forum.domain.usuario.UsuarioRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository repository;

	@PostMapping
	@Transactional
	public ResponseEntity cadastrar(@RequestBody @Valid DadosUsuario dados, UriComponentsBuilder uriBuilder) {
		if (repository.findByEmail(dados.email()) == null && repository.findByNome(dados.nome()) == null) {
			var usuario = new Usuario(dados);
			repository.save(usuario);
		
			var uri = uriBuilder.path("/login/{id}").buildAndExpand(usuario.getId()).toUri();
		
			return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Este email ou nome de usuário ja esta em uso!");
	}
	
	@GetMapping
	public ResponseEntity<Page<DadosListagemUsuario>> listar(@RequestParam(required = false) String nomeUsuario,
			@PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {
		
		Page<Usuario> page;
		
		if (nomeUsuario != null) {
			page = repository.findByNome(nomeUsuario, paginacao);
		} else {
			page = repository.findAll(paginacao);
		}
		
		var detalhamentoUsuarios = page.map(usuario -> new DadosListagemUsuario(usuario));
		
		return ResponseEntity.ok(detalhamentoUsuarios);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity detalhar(@PathVariable Long id) {
		var usuario = repository.getReferenceById(id);
		
		return ResponseEntity.ok(new DadosListagemUsuario(usuario));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoUsuario dados) {
		if (repository.findByNome(dados.nome()) == null && repository.findByEmail(dados.email()) == null) {
			var usuario = repository.getReferenceById(id);
			usuario.atualizarInformacoes(dados);
			return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário duplicado!");
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity excluir(@PathVariable Long id) {
		var usuario = repository.getReferenceById(id);
		usuario.excluir();
		
		return ResponseEntity.noContent().build();
	}

}
