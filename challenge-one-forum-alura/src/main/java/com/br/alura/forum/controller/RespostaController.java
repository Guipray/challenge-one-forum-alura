package com.br.alura.forum.controller;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.regex.Pattern;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.alura.forum.domain.curso.DadosCurso;
import com.br.alura.forum.domain.curso.DadosDetalhamentoCurso;
import com.br.alura.forum.domain.resposta.DadosAtualizacaoResposta;
import com.br.alura.forum.domain.resposta.DadosCadastroResposta;
import com.br.alura.forum.domain.resposta.DadosDetalhamentoResposta;
import com.br.alura.forum.domain.resposta.DadosListagemResposta;
import com.br.alura.forum.domain.resposta.Resposta;
import com.br.alura.forum.domain.resposta.RespostaDTO;
import com.br.alura.forum.domain.resposta.RespostaRepository;
import com.br.alura.forum.domain.topico.DadosAtualizacaoTopico;
import com.br.alura.forum.domain.topico.DadosDetalhamentoTopico;
import com.br.alura.forum.domain.topico.Topico;
import com.br.alura.forum.domain.topico.TopicoDTO;
import com.br.alura.forum.domain.topico.TopicoRepository;
import com.br.alura.forum.domain.usuario.DadosDetalhamentoUsuario;
import com.br.alura.forum.domain.usuario.DadosUsuario;
import com.br.alura.forum.domain.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("respostas")
@Tag(name = "Respostas", description = "CRUD completo das respostas")
public class RespostaController {

	@Autowired
	private RespostaRepository respostaRepository;

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@PostMapping
	@Transactional
	@Operation(summary = "Cadastra uma nova resposta", description = "Adiciona uma nova resposta ao fórum", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "201", description = "Resposta criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosDetalhamentoResposta.class)))
	@ApiResponse(responseCode = "405", description = "Entrada inválida")
	public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroResposta dados, UriComponentsBuilder uriBuilder) {
		var topico = topicoRepository.findByTitulo(dados.topico().titulo());
		var autor = usuarioRepository.findByEmail(dados.autor().email());

		if (topico != null && autor != null) {
			var resposta = new Resposta(dados);

			resposta.setTopico(topico);

			resposta.setAutor(autor);

			respostaRepository.save(resposta);

			var uri = uriBuilder.path("/respostas/{id}").buildAndExpand(resposta.getId()).toUri();

			var respostaDTO = new RespostaDTO(resposta);
			var dadosResposta = new DadosDetalhamentoResposta(respostaDTO);

			topico.setResposta(resposta);

			topicoRepository.save(topico);

			return ResponseEntity.created(uri).body(dadosResposta);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário ou Tópico não encontrado!");
	}

	@GetMapping
	@Operation(summary = "Lista todas as respostas", description = "Retorna uma lista com todos as respostas", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "200", description = "Listagem bem sucedida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosListagemResposta.class)))
	public ResponseEntity<Page<DadosListagemResposta>> listar(@RequestParam(required = false) String anoCriacao,
			@PageableDefault(size = 10, sort = { "dataCriacao" }) Pageable paginacao) {

		Page<Resposta> page;

		int year = 0;

		if (anoCriacao != null) {
			year = Integer.parseInt(anoCriacao);
		}

		LocalDateTime dataInicio = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0);
		LocalDateTime dataFim = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59);

		if (anoCriacao != null) {
			if (!Pattern.matches("\\d{4}", anoCriacao)) {
				return ResponseEntity.badRequest().build();
			}
			page = respostaRepository.findByAtivoTrueAndDataCriacaoBetween(dataInicio, dataFim, paginacao);
		} else {
			page = respostaRepository.findAllByAtivoTrue(paginacao);
		}

		var detalhamentoRespostas = page.map(resposta -> new DadosListagemResposta(new RespostaDTO(resposta)));

		return ResponseEntity.ok(detalhamentoRespostas);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Detalha uma resposta pelo id", description = "Retorna todos os atributos da resposta", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "200", description = "Detalhamento bem sucedido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosDetalhamentoResposta.class)))
	public ResponseEntity detalhar(@PathVariable Long id) {
		var resposta = respostaRepository.getReferenceById(id);

		return ResponseEntity.ok(new DadosDetalhamentoResposta(new RespostaDTO(resposta)));
	}

	@PutMapping("/{id}")
	@Transactional
	@Operation(summary = "Atualiza uma resposta pelo id", description = "Atualiza as informações da resposta", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponse(responseCode = "200", description = "Atualização bem sucedida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosDetalhamentoResposta.class)))
	public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoResposta dados) {
		var resposta = respostaRepository.getReferenceById(id);
		resposta.atualizarInformacoes(dados);

		return ResponseEntity.ok(new DadosDetalhamentoResposta(resposta));
	}

	@DeleteMapping("/{id}")
	@Transactional
	@Operation(summary = "Exclui uma resposta pelo id", description = "Deleta uma resposta", security = { @SecurityRequirement(name = "bearer-key") })
	public ResponseEntity excluir(@PathVariable Long id) {
		var resposta = respostaRepository.getReferenceById(id);
		resposta.excluir();

		return ResponseEntity.noContent().build();
	}

}
