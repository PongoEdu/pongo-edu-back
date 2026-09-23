package com.pongo.pongoedu.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RoteiroMaterialRequest(
        @NotNull Long produtoId,
        @NotNull @Positive Integer quantidadeNecessaria,
        @Size(max = 255) String observacao
) {
}
