package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.domain.entity.Usuario;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SessaoService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Usuario usuarioAtual() {
        var autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null || autenticacao.getName() == null) {
            throw new DomainException("Usuario nao autenticado");
        }
        return usuarioRepository.findByEmailIgnoreCase(autenticacao.getName())
                .orElseThrow(() -> ResourceNotFoundException.of("Usuario", autenticacao.getName()));
    }
}
