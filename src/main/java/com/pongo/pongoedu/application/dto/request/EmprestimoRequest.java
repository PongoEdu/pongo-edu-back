package com.pongo.pongoedu.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EmprestimoRequest(
        @NotNull Long produtoId,
        @NotNull @Positive Integer quantidade,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataFim,
        @Size(max = 255) String finalidade
) {
}
