package com.br.alura.forum.domain.curso;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "cursos")
@Entity(name = "Curso")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String categoria;
	private Boolean ativo = true;

	public Curso(DadosCurso curso) {
		this.nome = curso.nome();
		this.categoria = curso.categoria();
	}

	public void atualizaInformacoes(DadosCurso dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
		if (dados.categoria() != null) {
			this.categoria = dados.categoria();
		}
	}

	public void atualizarInformacoes(@Valid DadosAtualizacaoCurso dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
		if (dados.categoria() != null) {
			this.categoria = dados.categoria();
		}
	}

	public void excluir() {
		this.ativo = false;
	}	
}
