package com.pongo.pongoedu.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record LoteRequest(
        @NotBlank @Size(max = 60) String codigo,
        @Size(max = 150) String fornecedor,
        LocalDate dataFabricacao,
        LocalDate dataValidade,
        @Size(max = 255) String observacao
) {
}
