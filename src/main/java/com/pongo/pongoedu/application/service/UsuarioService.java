package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.request.UsuarioAtualizacaoRequest;
import com.pongo.pongoedu.application.dto.request.UsuarioRequest;
import com.pongo.pongoedu.application.dto.response.UsuarioResponse;
import com.pongo.pongoedu.domain.entity.Usuario;
import com.pongo.pongoedu.domain.enums.Perfil;
import com.pongo.pongoedu.domain.exception.ConflitoException;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.AgendamentoRepository;
import com.pongo.pongoedu.domain.repository.MovimentacaoEstoqueRepository;
import com.pongo.pongoedu.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponse criar(UsuarioRequest request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ConflitoException("O e-mail " + request.email() + " ja pertence a outro usuario.");
        }
        var usuario = usuarioRepository.save(Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .perfil(request.perfil())
                .ativo(true)
                .build());
        return UsuarioResponse.de(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar(Perfil perfil) {
        var usuarios = perfil == null ? usuarioRepository.findAll() : usuarioRepository.findByPerfil(perfil);
        return usuarios.stream().map(UsuarioResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscar(Long id) {
        return UsuarioResponse.de(buscarEntidade(id));
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioAtualizacaoRequest request) {
        var usuario = buscarEntidade(id);
        boolean emailMudou = !usuario.getEmail().equalsIgnoreCase(request.email());
        if (emailMudou && usuarioRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ConflitoException("O e-mail " + request.email() + " ja pertence a outro usuario.");
        }
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setPerfil(request.perfil());
        usuario.setAtivo(request.ativo());
        if (request.senha() != null && !request.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.senha()));
        }
        return UsuarioResponse.de(usuarioRepository.save(usuario));
    }

    /**
     * Excecao 4a/4b do RF19: usuario com historico e inativado em vez de excluido.
     */
    @Transactional
    public UsuarioResponse remover(Long id) {
        var usuario = buscarEntidade(id);
        boolean possuiVinculo = agendamentoRepository.existsByProfessorId(id)
                || movimentacaoRepository.existsByUsuarioId(id);

        if (!possuiVinculo) {
            usuarioRepository.delete(usuario);
            return UsuarioResponse.de(usuario);
        }

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new DomainException("O usuario ja esta inativo.");
        }
        usuario.setAtivo(false);
        return UsuarioResponse.de(usuarioRepository.save(usuario));
    }

    public Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Usuario", id));
    }
}
