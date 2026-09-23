package com.pongo.pongoedu.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @NotBlank @Size(max = 100) String nome,
        @Size(max = 255) String descricao,
        @NotNull Boolean permiteEmprestimo
) {
}
