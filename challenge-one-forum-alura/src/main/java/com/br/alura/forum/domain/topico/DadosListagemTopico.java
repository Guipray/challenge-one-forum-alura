package com.br.alura.forum.domain.topico;

import java.time.LocalDateTime;

import com.br.alura.forum.domain.curso.Curso;

public record DadosListagemTopico(Long id, String titulo, String mensagem, LocalDateTime dataCriacao, StatusTopico status, DadosDetalhamentoAutor autor, Curso curso) {

	public DadosListagemTopico(TopicoDTO topicoDTO) {
		this(topicoDTO.getId(), topicoDTO.getTitulo(), topicoDTO.getMensagem(), topicoDTO.getDataCriacao(), topicoDTO.getStatus(), topicoDTO.getAutor(), topicoDTO.getCurso());
	}

}
