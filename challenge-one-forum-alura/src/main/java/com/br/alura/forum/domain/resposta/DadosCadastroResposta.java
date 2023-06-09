package com.br.alura.forum.domain.resposta;

import com.br.alura.forum.domain.topico.DadosCadastroTopico;
import com.br.alura.forum.domain.usuario.DadosUsuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroResposta(
		@NotBlank
		String mensagem,
		@NotNull
		@Valid
		DadosCadastroTopico topico,
		@NotNull
		@Valid
		DadosUsuario autor) {
}
