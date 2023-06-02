package com.br.alura.forum.domain.resposta;

import java.time.LocalDateTime;

import com.br.alura.forum.domain.topico.DadosDetalhamentoAutor;
import com.br.alura.forum.domain.topico.DadosListagemTopico;

public record DadosListagemResposta(Long id, String mensagem, DadosListagemTopico topico, LocalDateTime dataCriacao, DadosDetalhamentoAutor autor) {

	public DadosListagemResposta(RespostaDTO respostaDTO) {
		this(respostaDTO.getId(), respostaDTO.getMensagem(), respostaDTO.getTopico(), respostaDTO.getDataCriacao(), respostaDTO.getAutor());
	}

}
