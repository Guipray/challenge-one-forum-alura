package com.br.alura.forum.domain.curso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
	
	Curso findByNomeAndCategoria(String nome, String categoria);

	Page<Curso> findByAtivoTrueAndCategoria(String categoria, Pageable paginacao);

	Page<Curso> findAllByAtivoTrue(Pageable paginacao);
	
}
