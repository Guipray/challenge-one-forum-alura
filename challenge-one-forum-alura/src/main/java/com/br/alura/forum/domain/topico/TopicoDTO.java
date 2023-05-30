package com.br.alura.forum.domain.topico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.br.alura.forum.domain.curso.Curso;
import com.br.alura.forum.domain.resposta.Resposta;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TopicoDTO {

	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao = LocalDateTime.now();
	private StatusTopico status = StatusTopico.NAO_RESPONDIDO;
	private DadosDetalhamentoAutor autor;
	private Curso curso;
	private List<Resposta> respostas = new ArrayList<>();
	
	public TopicoDTO(Topico topico) {
		this.id = topico.getId();
		this.titulo = topico.getTitulo();
		this.mensagem = topico.getMensagem();
		this.dataCriacao = topico.getDataCriacao();
		this.status = topico.getStatus();
		this.autor = new DadosDetalhamentoAutor(topico.getAutor());
		this.curso = topico.getCurso();
		this.respostas = topico.getRespostas();
	}
	
}
