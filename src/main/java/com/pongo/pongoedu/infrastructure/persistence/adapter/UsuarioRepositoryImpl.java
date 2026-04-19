package com.pongo.pongoedu.infrastructure.persistence.adapter;

import com.pongo.pongoedu.domain.entities.Usuario;
import com.pongo.pongoedu.domain.repository.UsuarioRepository;
import com.pongo.pongoedu.infrastructure.persistence.jpa.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final UsuarioJpaRepository usuarioJpaRepository;

    @Override
    public Usuario salvar(Usuario usuario) {
        return usuarioJpaRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioJpaRepository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioJpaRepository.findByEmail(email);
    }

    @Override
    public void deletar(Long id) {
        usuarioJpaRepository.deleteById(id);
    }
}

