package com.br.alura.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.alura.forum.domain.curso.CursoRepository;
import com.br.alura.forum.domain.curso.DadosDetalhamentoCurso;
import com.br.alura.forum.domain.resposta.DadosListagemResposta;
import com.br.alura.forum.domain.topico.DadosAtualizacaoTopico;
import com.br.alura.forum.domain.topico.DadosCadastroTopico;
import com.br.alura.forum.domain.topico.DadosDetalhamentoTopico;
import com.br.alura.forum.domain.topico.DadosListagemTopico;
import com.br.alura.forum.domain.topico.Topico;
import com.br.alura.forum.domain.topico.TopicoDTO;
import com.br.alura.forum.domain.topico.TopicoRepository;
import com.br.alura.forum.domain.usuario.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("topicos")
@Tag(name = "Tópicos", description = "CRUD completo dos tópicos")
public class TopicoController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CursoRepository cursoRepository;

	@PostMapping
	@Transactional
	@Operation(summary = "Cadastra um novo tópico", description = "Adiciona um novo tópico fórum", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "201", description = "Tópico criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosDetalhamentoTopico.class)))
	@ApiResponse(responseCode = "405", description = "Entrada inválida")
	public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroTopico dados, UriComponentsBuilder uriBuilder) {
		
		if (topicoRepository.findByTitulo(dados.titulo()) == null && topicoRepository.findByMensagem(dados.mensagem()) == null) {
			var usuario = usuarioRepository.findByEmail(dados.autor().email());

			var curso = cursoRepository.findByNomeAndCategoria(dados.curso().nome(), dados.curso().categoria());

			var topico = new Topico(dados);
			topico.setAutor(usuario);
			topico.setCurso(curso);
			topicoRepository.save(topico);

			var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

			var topicoDTO = new TopicoDTO(topico);

			return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topicoDTO));
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Tópico duplicado!");

	}

	@GetMapping
	@Operation(summary = "Lista todos os tópicos", description = "Retorna uma lista com todos os tópicos", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "200", description = "Listagem bem sucedida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosListagemTopico.class)))
	public ResponseEntity<Page<DadosListagemTopico>> listar(@RequestParam(required = false) String nomeCurso,
			@RequestParam(required = false) String anoCriacao,
			@PageableDefault(size = 10, sort = { "dataCriacao" }) Pageable paginacao) {

		Page<Topico> page;
		
		int year = 0;

		if (anoCriacao != null) {
			year = Integer.parseInt(anoCriacao);
		}
		
		LocalDateTime dataInicio = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0);
		LocalDateTime dataFim = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59);

		if (nomeCurso != null && anoCriacao != null) {
			if (!Pattern.matches("\\d{4}", anoCriacao)) {
				return ResponseEntity.badRequest().build();
			}
			page = topicoRepository.findByAtivoTrueAndCursoNomeAndDataCriacaoBetween(nomeCurso, dataInicio, dataFim, paginacao);
		} else if (nomeCurso != null) {
			page = topicoRepository.findByAtivoTrueAndCursoNome(nomeCurso, paginacao);
		} else if (anoCriacao != null) {
			page = topicoRepository.findByAtivoTrueAndDataCriacaoBetween(dataInicio, dataFim, paginacao);
		} else {
			page = topicoRepository.findAllByAtivoTrue(paginacao);
		}

		var detalhamentoTopicos = page.map(topico -> new DadosListagemTopico(new TopicoDTO(topico)));

		return ResponseEntity.ok(detalhamentoTopicos);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Detalha um tópico pelo id", description = "Retorna todos os atributos do tópico", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "200", description = "Detalhamento bem sucedido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosListagemTopico.class)))
	public ResponseEntity detalhar(@PathVariable Long id) {
		var topico = topicoRepository.getReferenceById(id);
		
		return ResponseEntity.ok(new DadosListagemTopico(new TopicoDTO(topico)));
	}
	
	@PutMapping("/{id}")
	@Transactional
	@Operation(summary = "Atualiza um tópico pelo id", description = "Atualiza as informações do tópico", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "200", description = "Atualização bem sucedida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosDetalhamentoTopico.class)))
	public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoTopico dados) {
		if (topicoRepository.findByTitulo(dados.titulo()) == null && topicoRepository.findByMensagem(dados.mensagem()) == null) {
			var topico = topicoRepository.getReferenceById(id);
			topico.atualizarInformacoes(dados);
			return ResponseEntity.ok(new DadosDetalhamentoTopico(new TopicoDTO(topico)));
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Tópicos Duplicados!");
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@Operation(summary = "Exclui um tópico pelo id", description = "Deleta um tópico", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity excluir(@PathVariable Long id) {
		var topico = topicoRepository.getReferenceById(id);
		topico.excluir();
		
		return ResponseEntity.noContent().build();
	}

}
