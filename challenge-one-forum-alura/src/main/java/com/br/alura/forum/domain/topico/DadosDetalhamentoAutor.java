package com.br.alura.forum.domain.topico;

import com.br.alura.forum.domain.usuario.Usuario;

public record DadosDetalhamentoAutor(Long id ,String nome, String email) {

	public DadosDetalhamentoAutor(Usuario autor) {
		this(autor.getId(), autor.getNome(), autor.getEmail());
	}
}
