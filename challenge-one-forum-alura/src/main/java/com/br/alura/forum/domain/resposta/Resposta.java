package com.br.alura.forum.domain.resposta;

import java.time.LocalDateTime;

import com.br.alura.forum.domain.curso.DadosCurso;
import com.br.alura.forum.domain.topico.DadosAtualizacaoTopico;
import com.br.alura.forum.domain.topico.StatusTopico;
import com.br.alura.forum.domain.topico.Topico;
import com.br.alura.forum.domain.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "respostas")
@Entity(name = "Resposta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Resposta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String mensagem;
	
	@ManyToOne
	@JoinColumn(name = "topico_id")
	private Topico topico;
	
	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao = LocalDateTime.now();
	
	@ManyToOne
	private Usuario autor;
	private Boolean solucao = false;
	private Boolean ativo = true;
	
	public Resposta(@Valid DadosCadastroResposta dados) {
		this.mensagem = dados.mensagem();
		this.topico = new Topico(dados.topico());
		this.autor = new Usuario(dados.autor());
	}

	public void setTopico(Topico topico) {
		this.topico = topico;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	public void atualizarInformacoes(DadosAtualizacaoResposta dados) {
		if (dados.mensagem() != null) {
			this.mensagem = dados.mensagem();
		}
		if (dados.status() != null) {
			if (dados.status() == StatusTopico.FECHADO || dados.status() == StatusTopico.SOLUCIONADO) {
				this.solucao = true;
				this.topico.atualizarStatus(dados.status());
			} else {
				this.solucao = false;
				this.topico.atualizarStatus(dados.status());
			}
			
		}
	}

	public void excluir() {
		this.ativo = false;
	}
}
