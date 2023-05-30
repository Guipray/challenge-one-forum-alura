package com.br.alura.forum.domain.curso;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
	
	Curso findByNomeAndCategoria(String nome, String categoria);
	
}
