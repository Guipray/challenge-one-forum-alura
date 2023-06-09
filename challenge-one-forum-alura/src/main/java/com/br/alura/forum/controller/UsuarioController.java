package com.br.alura.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import com.br.alura.forum.domain.usuario.DadosAtualizacaoUsuario;
import com.br.alura.forum.domain.usuario.DadosDetalhamentoUsuario;
import com.br.alura.forum.domain.usuario.DadosListagemUsuario;
import com.br.alura.forum.domain.usuario.DadosUsuario;
import com.br.alura.forum.domain.usuario.Usuario;
import com.br.alura.forum.domain.usuario.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("usuarios")
@Tag(name = "Usuários", description = "CRUD completo dos usuários")
public class UsuarioController {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping
	@Transactional
	@Operation(summary = "Cadastra um novo usuário", description = "Adiciona um novo usuário ao banco de dados")
	@ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosDetalhamentoUsuario.class)))
	@ApiResponse(responseCode = "405", description = "Entrada inválida")
	public ResponseEntity cadastrar(@RequestBody @Valid DadosUsuario dados, UriComponentsBuilder uriBuilder) {
		if (repository.findByEmail(dados.email()) == null && repository.findByNome(dados.nome()) == null) {
			String encryptedPassword = passwordEncoder.encode(dados.senha());
			var usuario = new Usuario(dados);
			usuario.setSenha(encryptedPassword);
			
			repository.save(usuario);
		
			var uri = uriBuilder.path("/login/{id}").buildAndExpand(usuario.getId()).toUri();
		
			return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Este email ou nome de usuário ja esta em uso!");
	}
	
	@GetMapping
	@Operation(summary = "Lista todos os usuários", description = "Retorna uma lista com todos os usuários", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "200", description = "Listagem bem sucedida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosListagemUsuario.class)))
	public ResponseEntity<Page<DadosListagemUsuario>> listar(@RequestParam(required = false) String nomeUsuario,
			@PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {
		
		Page<Usuario> page;
		
		if (nomeUsuario != null) {
			page = repository.findByAtivoTrueAndNome(nomeUsuario, paginacao);
		} else {
			page = repository.findAllByAtivoTrue(paginacao);
		}
		
		var detalhamentoUsuarios = page.map(usuario -> new DadosListagemUsuario(usuario));
		
		return ResponseEntity.ok(detalhamentoUsuarios);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Detalha um usuário pelo id", description = "Retorna todos os atributos do usuário", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "200", description = "Detalhamento bem sucedido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosListagemUsuario.class)))
	public ResponseEntity detalhar(@PathVariable Long id) {
		var usuario = repository.getReferenceById(id);
		
		return ResponseEntity.ok(new DadosListagemUsuario(usuario));
	}
	
	@PutMapping("/{id}")
	@Transactional
	@Operation(summary = "Atualiza um usuário pelo id", description = "Atualiza as informações do usuário", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "200", description = "Atualização bem sucedida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosDetalhamentoUsuario.class)))
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
	@Operation(summary = "Exclui um usuário pelo id", description = "Deleta um usuário", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity excluir(@PathVariable Long id) {
		var usuario = repository.getReferenceById(id);
		usuario.excluir();
		
		return ResponseEntity.noContent().build();
	}

}
