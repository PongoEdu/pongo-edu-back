package com.pongo.pongoedu.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RecebimentoItemRequest(
        @NotNull Long produtoId,
        @NotNull Long loteId,
        @NotNull @Positive Integer quantidade,
        BigDecimal valorUnitario,
        Boolean confirmarVencido
) {
}
