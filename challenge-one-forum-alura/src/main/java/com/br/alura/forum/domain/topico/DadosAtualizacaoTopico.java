package com.br.alura.forum.domain.topico;

import com.br.alura.forum.domain.curso.DadosCurso;
import com.br.alura.forum.domain.resposta.Resposta;
import com.br.alura.forum.domain.usuario.DadosUsuario;

import jakarta.validation.Valid;

public record DadosAtualizacaoTopico(
		String titulo,
		String mensagem,
		StatusTopico status,
		@Valid
		DadosUsuario autor,
		@Valid
		DadosCurso curso) {
}
