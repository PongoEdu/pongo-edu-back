package com.pongo.pongoedu.application.dto.request;

import com.pongo.pongoedu.domain.enums.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioAtualizacaoRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotNull Perfil perfil,
        @NotNull Boolean ativo,
        String senha
) {
}
