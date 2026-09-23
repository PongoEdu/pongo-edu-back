package com.pongo.pongoedu.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoRequest(
        @NotNull Long laboratorioId,
        Long roteiroId,
        @Size(max = 60) String turma,
        @NotNull LocalDate data,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFim,
        @Size(max = 255) String observacao
) {
}
