package com.br.alura.forum.domain.topico;

import com.br.alura.forum.domain.curso.DadosCurso;
import com.br.alura.forum.domain.usuario.DadosUsuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroTopico(
		@NotBlank
		String titulo,
		@NotBlank
		String mensagem,
		@NotNull
		@Valid
		DadosUsuario autor,
		@NotNull
		@Valid
		DadosCurso curso) {
}
