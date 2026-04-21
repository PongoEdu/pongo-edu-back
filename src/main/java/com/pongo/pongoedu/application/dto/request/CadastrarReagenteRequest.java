package com.pongo.pongoedu.application.dto.request;

import com.pongo.pongoedu.domain.enums.TipoMaterial;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastrarReagenteRequest(
        @NotBlank String nome,
        String formula,
        String descricao,
        @NotNull @Min(0) Integer quantidade,
        @NotNull @Min(0) Integer quantidadeMinima,
        String unidade,
        String localizacao,
        TipoMaterial tipo
) {}