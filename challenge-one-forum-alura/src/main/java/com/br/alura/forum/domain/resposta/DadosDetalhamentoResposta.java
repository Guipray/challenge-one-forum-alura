package com.br.alura.forum.domain.resposta;

import java.time.LocalDateTime;

import com.br.alura.forum.domain.topico.DadosDetalhamentoAutor;
import com.br.alura.forum.domain.topico.DadosListagemTopico;
import com.br.alura.forum.domain.topico.TopicoDTO;

public record DadosDetalhamentoResposta(Long id, String mensagem, DadosListagemTopico topico, LocalDateTime dataCriacao, DadosDetalhamentoAutor autor) {

	public DadosDetalhamentoResposta(RespostaDTO resposta) {
		this(resposta.getId(), resposta.getMensagem(), resposta.getTopico(), resposta.getDataCriacao(), resposta.getAutor());
	}
	
	public DadosDetalhamentoResposta(Resposta resposta) {
		this(resposta.getId(), resposta.getMensagem(), new DadosListagemTopico(new TopicoDTO(resposta.getTopico())), resposta.getDataCriacao(), new DadosDetalhamentoAutor(resposta.getAutor()));
	}

}
