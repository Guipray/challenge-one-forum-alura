package com.br.alura.forum.domain.topico;

import java.time.LocalDateTime;
import java.util.List;

import com.br.alura.forum.domain.curso.Curso;
import com.br.alura.forum.domain.resposta.Resposta;

public record DadosDetalhamentoTopico(Long id, String titulo, String mensagem, LocalDateTime dataCriacao, StatusTopico status, DadosDetalhamentoAutor autor, Curso curso, List<Resposta> respostas) {

	public DadosDetalhamentoTopico(TopicoDTO topico) {
		this(topico.getId(), topico.getTitulo(), topico.getMensagem(), topico.getDataCriacao(), topico.getStatus(), topico.getAutor(), topico.getCurso(), topico.getRespostas());
	}

}
