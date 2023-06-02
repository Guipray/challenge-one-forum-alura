package com.br.alura.forum.domain.resposta;

import java.time.LocalDateTime;

import com.br.alura.forum.domain.topico.DadosDetalhamentoAutor;
import com.br.alura.forum.domain.topico.DadosDetalhamentoTopico;
import com.br.alura.forum.domain.topico.DadosListagemTopico;
import com.br.alura.forum.domain.topico.TopicoDTO;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class RespostaDTO {

	private Long id;
	private String mensagem;
	private DadosListagemTopico topico;
	private LocalDateTime dataCriacao;
	private DadosDetalhamentoAutor autor;
	
	public RespostaDTO(Resposta resposta) {
		this.id = resposta.getId();
		this.mensagem = resposta.getMensagem();
		this.topico = new DadosListagemTopico(new TopicoDTO(resposta.getTopico()));
		this.dataCriacao = resposta.getDataCriacao();
		this.autor = new DadosDetalhamentoAutor(resposta.getAutor());
	}
	
}
