package com.pongo.pongoedu.application.usecase.auth;

import com.pongo.pongoedu.application.dto.request.RegistrarUsuarioRequest;
import com.pongo.pongoedu.application.dto.response.TokenResponse;
import com.pongo.pongoedu.application.port.JwtProvider;
import com.pongo.pongoedu.domain.entities.Usuario;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse executar(RegistrarUsuarioRequest request) {
        if (usuarioRepository.buscarPorEmail(request.getEmail()).isPresent()) {
            throw new DomainException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setNome(request.getNome());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setRole(request.getRole());
        usuario.setAtivo(true);

        usuario = usuarioRepository.salvar(usuario);

        String token = jwtProvider.gerarToken(usuario.getId(), usuario.getEmail());
        return new TokenResponse(token, "Bearer", jwtProvider.obterTempoExpiracao());
    }
}