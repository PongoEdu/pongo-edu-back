package com.pongo.pongoedu.application.dto.request;

import com.pongo.pongoedu.domain.enums.UnidadeMedida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProdutoRequest(
        @NotBlank @Size(max = 60) String codigo,
        @NotBlank @Size(max = 150) String nome,
        @Size(max = 255) String descricao,
        @NotNull Long categoriaId,
        @NotNull Long loteId,
        @NotNull UnidadeMedida unidadeMedida,
        @NotNull @PositiveOrZero Integer quantidadeEstoque,
        @NotNull @PositiveOrZero Integer estoqueMinimo,
        @Size(max = 120) String localizacao
) {
}
