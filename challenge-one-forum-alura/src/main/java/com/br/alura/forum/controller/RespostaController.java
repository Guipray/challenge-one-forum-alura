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

import io.swagger.annotations.ApiOperation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("respostas")
public class RespostaController {

	@Autowired
	private RespostaRepository respostaRepository;

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@ApiOperation("Cadastrar Resposta")
	@PostMapping
	@Transactional
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

	@ApiOperation("Listar Resposta")
	@GetMapping
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
			page = respostaRepository.findByDataCriacaoBetween(dataInicio, dataFim, paginacao);
		} else {
			page = respostaRepository.findAll(paginacao);
		}

		var detalhamentoRespostas = page.map(resposta -> new DadosListagemResposta(new RespostaDTO(resposta)));
		System.out.println(detalhamentoRespostas);

		return ResponseEntity.ok(detalhamentoRespostas);
	}

	@ApiOperation("Detalhar Resposta")
	@GetMapping("/{id}")
	public ResponseEntity detalhar(@PathVariable Long id) {
		var resposta = respostaRepository.getReferenceById(id);

		return ResponseEntity.ok(new DadosDetalhamentoResposta(new RespostaDTO(resposta)));
	}

	@ApiOperation("Atualizar Resposta")
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoResposta dados) {
		var resposta = respostaRepository.getReferenceById(id);
		resposta.atualizarInformacoes(dados);

		return ResponseEntity.ok(new DadosDetalhamentoResposta(resposta));
	}

	@ApiOperation("Excluir Resposta")
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity excluir(@PathVariable Long id) {
		var resposta = respostaRepository.getReferenceById(id);
		resposta.excluir();

		return ResponseEntity.noContent().build();
	}

}
