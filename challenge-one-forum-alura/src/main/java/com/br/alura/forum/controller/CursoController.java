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

import io.swagger.annotations.ApiOperation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("cursos")
public class CursoController {

	@Autowired
	private CursoRepository repository;

	@PostMapping
	@Transactional
	public ResponseEntity cadastrar(@RequestBody @Valid DadosCurso dados, UriComponentsBuilder uriBuilder) {
		var curso = new Curso(dados);
		repository.save(curso);

		var uri = uriBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();

		return ResponseEntity.created(uri).body(new DadosDetalhamentoCurso(curso));
	}

	@ApiOperation("Listar Cursos")
	@GetMapping
	public ResponseEntity<Page<DadosListagemCurso>> listar(@RequestParam(required = false) String categoria,
			@PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {

		Page<Curso> page;

		if (categoria != null) {
			page = repository.findByCategoria(categoria, paginacao);
		} else {
			page = repository.findAll(paginacao);
		}

		var detalhamentoCurso = page.map(DadosListagemCurso::new);

		return ResponseEntity.ok(detalhamentoCurso);

	}

	@ApiOperation("Detalhar Curso")
	@GetMapping("/{id}")
	public ResponseEntity detalhar(@PathVariable Long id) {
		var curso = repository.getReferenceById(id);

		return ResponseEntity.ok(new DadosDetalhamentoCurso(curso));
	}

	@ApiOperation("Atualizar Curso")
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity atualizar(@PathVariable Long id, @RequestBody DadosAtualizacaoCurso dados) {
		var curso = repository.getReferenceById(id);
		curso.atualizarInformacoes(dados);

		return ResponseEntity.ok(new DadosDetalhamentoCurso(curso));
	}

	@ApiOperation("Excluir Curso")
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity excluir(@PathVariable Long id) {
		var curso = repository.getReferenceById(id);
		curso.excluir();

		return ResponseEntity.noContent().build();
	}

}
