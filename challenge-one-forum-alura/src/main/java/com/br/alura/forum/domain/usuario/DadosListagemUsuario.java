package com.br.alura.forum.domain.usuario;

public record DadosListagemUsuario(String nome, String email) {

	public DadosListagemUsuario(Usuario dados) {
		this(dados.getNome(), dados.getEmail());
	}
}
