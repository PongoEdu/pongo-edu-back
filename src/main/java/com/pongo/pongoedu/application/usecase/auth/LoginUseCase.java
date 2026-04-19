package com.pongo.pongoedu.application.usecase.auth;

import com.pongo.pongoedu.application.dto.request.LoginRequest;
import com.pongo.pongoedu.application.dto.response.TokenResponse;
import com.pongo.pongoedu.application.port.JwtProvider;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {
    private final UsuarioRepository usuarioRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse executar(LoginRequest request) {
        var usuario = usuarioRepository.buscarPorEmail(request.getEmail())
                .orElseThrow(() -> ResourceNotFoundException.of("Usuário", request.getEmail()));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new DomainException("Credenciais inválidas");
        }

        String token = jwtProvider.gerarToken(usuario.getId(), usuario.getEmail());
        return new TokenResponse(token, "Bearer", jwtProvider.obterTempoExpiracao());
    }
}