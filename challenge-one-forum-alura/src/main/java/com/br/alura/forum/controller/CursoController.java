package com.br.alura.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.alura.forum.domain.curso.Curso;
import com.br.alura.forum.domain.curso.CursoRepository;
import com.br.alura.forum.domain.curso.DadosCurso;
import com.br.alura.forum.domain.curso.DadosDetalhamentoCurso;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("curso")
public class CursoController {
	
	@Autowired
	private CursoRepository repository;

	@PostMapping
	@Transactional
	public ResponseEntity cadastrar(@RequestBody @Valid DadosCurso dados, UriComponentsBuilder uriBuilder) {
		var curso = new Curso(dados);
		repository.save(curso);
		
		var uri = uriBuilder.path("/curso/{id}").buildAndExpand(curso.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new DadosDetalhamentoCurso(curso));
	}
	
}
