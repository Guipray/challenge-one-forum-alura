package com.br.alura.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	
	private List<Resposta> respostas = new ArrayList<>();

}
