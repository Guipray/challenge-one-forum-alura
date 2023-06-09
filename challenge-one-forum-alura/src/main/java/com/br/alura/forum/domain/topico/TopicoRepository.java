package com.br.alura.forum.domain.topico;

import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.alura.forum.domain.resposta.Resposta;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

	Page<Topico> findByAtivoTrueAndCursoNomeAndDataCriacaoBetween(String nomeCurso, LocalDateTime dataInicio,
			LocalDateTime dataFim, Pageable paginacao);

	Page<Topico> findByAtivoTrueAndCursoNome(String nomeCurso, Pageable paginacao);

	Page<Topico> findByAtivoTrueAndDataCriacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable paginacao);

	Topico findByTitulo(String titulo);

	Topico findByMensagem(String mensagem);

	Page<Topico> findAllByAtivoTrue(Pageable paginacao);

}
