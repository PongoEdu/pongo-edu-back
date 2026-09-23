package com.pongo.pongoedu.application.dto.request;

import com.pongo.pongoedu.domain.enums.TipoEntrada;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record RecebimentoRequest(
        @NotNull TipoEntrada tipoEntrada,
        @Size(max = 60) String numeroNotaFiscal,
        @Size(max = 150) String fornecedor,
        @NotNull LocalDate dataRecebimento,
        @Size(max = 255) String observacao,
        @NotEmpty @Valid List<RecebimentoItemRequest> itens
) {
}
