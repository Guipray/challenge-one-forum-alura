package com.br.alura.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.br.alura.forum.domain.curso.Curso;
import com.br.alura.forum.domain.curso.CursoRepository;
import com.br.alura.forum.domain.curso.DadosAtualizacaoCurso;
import com.br.alura.forum.domain.curso.DadosCurso;
import com.br.alura.forum.domain.curso.DadosDetalhamentoCurso;
import com.br.alura.forum.domain.curso.DadosListagemCurso;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("cursos")
@Tag(name = "Cursos", description = "CRUD completo dos cursos")
public class CursoController {

	@Autowired
	private CursoRepository repository;

	@PostMapping
	@Transactional
	@Operation(summary = "Cadastra um novo curso", description = "Adiciona um novo curso ao banco de dados", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "201", description = "Curso criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosDetalhamentoCurso.class)))
	@ApiResponse(responseCode = "405", description = "Entrada inválida")
	public ResponseEntity cadastrar(@RequestBody @Valid DadosCurso dados, UriComponentsBuilder uriBuilder) {
		var curso = new Curso(dados);
		repository.save(curso);

		var uri = uriBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();

		return ResponseEntity.created(uri).body(new DadosDetalhamentoCurso(curso));
	}

	@GetMapping
	@Operation(summary = "Lista todos os cursos", description = "Retorna uma lista com todos os cursos", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "200", description = "Listagem bem sucedida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosListagemCurso.class)))
	public ResponseEntity<Page<DadosListagemCurso>> listar(@RequestParam(required = false) String categoria,
			@PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {

		Page<Curso> page;

		if (categoria != null) {
			page = repository.findByAtivoTrueAndCategoria(categoria, paginacao);
		} else {
			page = repository.findAllByAtivoTrue(paginacao);
		}

		var detalhamentoCurso = page.map(DadosListagemCurso::new);

		return ResponseEntity.ok(detalhamentoCurso);

	}

	@GetMapping("/{id}")
	@Operation(summary = "Detalha um curso pelo id", description = "Retorna todos os atributos do curso", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "200", description = "Detalhamento bem sucedido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosDetalhamentoCurso.class)))
	public ResponseEntity detalhar(@PathVariable Long id) {
		var curso = repository.getReferenceById(id);

		return ResponseEntity.ok(new DadosDetalhamentoCurso(curso));
	}

	@PutMapping("/{id}")
	@Transactional
	@Operation(summary = "Atualiza um curso pelo id", description = "Atualiza as informações do curso", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "200", description = "Atualização bem sucedida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosDetalhamentoCurso.class)))
	public ResponseEntity atualizar(@PathVariable Long id, @RequestBody DadosAtualizacaoCurso dados) {
		var curso = repository.getReferenceById(id);
		curso.atualizarInformacoes(dados);

		return ResponseEntity.ok(new DadosDetalhamentoCurso(curso));
	}

	@DeleteMapping("/{id}")
	@Transactional
	@Operation(summary = "Exclui um curso pelo id", description = "Deleta um curso", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity excluir(@PathVariable Long id) {
		var curso = repository.getReferenceById(id);
		curso.excluir();

		return ResponseEntity.noContent().build();
	}

}
