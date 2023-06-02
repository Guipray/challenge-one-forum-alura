package com.br.alura.forum.domain.resposta;

import com.br.alura.forum.domain.topico.StatusTopico;

import jakarta.validation.constraints.NotBlank;

public record DadosAtualizacaoResposta(
		@NotBlank
		String mensagem,
		StatusTopico status) {
}
