package com.br.alura.forum.domain.curso;

import jakarta.validation.constraints.NotBlank;

public record DadosCurso(
		@NotBlank
		String nome,
		@NotBlank
		String categoria) {

}
