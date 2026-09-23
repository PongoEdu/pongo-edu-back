package com.pongo.pongoedu.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LaboratorioRequest(
        @NotBlank @Size(max = 120) String nome,
        @Size(max = 255) String descricao,
        @Positive Integer capacidade
) {
}
