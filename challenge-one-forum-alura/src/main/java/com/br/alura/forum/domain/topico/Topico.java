package com.br.alura.forum.domain.topico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.br.alura.forum.domain.curso.Curso;
import com.br.alura.forum.domain.resposta.Resposta;
import com.br.alura.forum.domain.usuario.DadosUsuario;
import com.br.alura.forum.domain.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private String mensagem;
	
	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao = LocalDateTime.now();
	
	@Enumerated(EnumType.STRING)
	private StatusTopico status = StatusTopico.NAO_RESPONDIDO;
	
	@ManyToOne
	private Usuario autor;
	
	@ManyToOne
	private Curso curso;
	
	@OneToMany(mappedBy = "topico")
	private List<Resposta> respostas = new ArrayList<>();
	
	private Boolean ativo = true;
	
	public Topico(@Valid DadosCadastroTopico dados) {
		this.titulo = dados.titulo();
		this.mensagem = dados.mensagem();
		this.autor = new Usuario(dados.autor());
		this.curso = new Curso(dados.curso());
	}

	public void setAutor(Usuario usuario) {
		this.autor = usuario;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public void atualizarInformacoes(@Valid DadosAtualizacaoTopico dados) {
		if (dados.titulo() != null) {
			this.titulo = dados.titulo();
		}
		if (dados.mensagem() != null) {
			this.mensagem = dados.mensagem();
		}
		if (dados.status() != null) {
			this.status = dados.status();
		}
		if (dados.autor() != null) {
			this.autor.atualizarInformacoes(dados.autor());
		}
		if (dados.curso() != null) {
			this.curso.atualizaInformacoes(dados.curso());
		}
	}

	public void excluir() {
		this.ativo = false;
	}

	public void setResposta(Resposta resposta) {
		this.respostas.add(resposta);
	}

	public void atualizarStatus(StatusTopico status) {
		this.status = status;
	}

}
