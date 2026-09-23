package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.Usuario;
import com.pongo.pongoedu.domain.enums.Perfil;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        Perfil perfil,
        Boolean ativo,
        LocalDateTime criadoEm
) {
    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getAtivo(),
                usuario.getCriadoEm()
        );
    }
}
