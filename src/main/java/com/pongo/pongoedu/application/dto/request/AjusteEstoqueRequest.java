package com.pongo.pongoedu.application.dto.request;

import com.pongo.pongoedu.domain.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AjusteEstoqueRequest(
        @NotNull Long produtoId,
        @NotNull TipoMovimentacao tipo,
        @NotNull @Positive Integer quantidade,
        @Size(max = 255) String observacao
) {
}
