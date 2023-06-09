package com.br.alura.forum.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Usuario findByEmail(String email);

	Usuario findByNome(String nome);

	Page<Usuario> findByAtivoTrueAndNome(String nomeUsuario, Pageable paginacao);

	Page<Usuario> findAllByAtivoTrue(Pageable paginacao);
	
}
