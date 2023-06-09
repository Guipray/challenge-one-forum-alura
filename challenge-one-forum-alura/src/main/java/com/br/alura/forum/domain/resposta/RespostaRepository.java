package com.br.alura.forum.domain.resposta;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespostaRepository extends JpaRepository<Resposta, Long>{

	Page<Resposta> findByAtivoTrueAndDataCriacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim,
			Pageable paginacao);

	Page<Resposta> findAllByAtivoTrue(Pageable paginacao);
}
