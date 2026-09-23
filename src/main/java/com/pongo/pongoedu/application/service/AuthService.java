package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.request.LoginRequest;
import com.pongo.pongoedu.application.dto.response.TokenResponse;
import com.pongo.pongoedu.application.dto.response.UsuarioResponse;
import com.pongo.pongoedu.application.port.JwtProvider;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public TokenResponse autenticar(LoginRequest request) {
        var usuario = usuarioRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new DomainException("E-mail ou senha invalidos"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new DomainException("E-mail ou senha invalidos");
        }
        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new DomainException("Usuario inativo. Procure o auxiliar de laboratorio.");
        }

        var token = jwtProvider.gerarToken(usuario.getId(), usuario.getEmail());
        return TokenResponse.de(token, jwtProvider.obterTempoExpiracao(), UsuarioResponse.de(usuario));
    }
}
