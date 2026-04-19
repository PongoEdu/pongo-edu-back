package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entities.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    void deletar(Long id);
}
