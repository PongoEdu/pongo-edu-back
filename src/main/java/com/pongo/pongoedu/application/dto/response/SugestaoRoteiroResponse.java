package com.pongo.pongoedu.application.dto.response;

import java.util.List;

public record SugestaoRoteiroResponse(
        String titulo,
        String objetivo,
        String procedimento,
        Integer tempoEstimadoMinutos,
        List<RoteiroMaterialSugerido> materiaisSugeridos,
        String observacao
) {
    public record RoteiroMaterialSugerido(
            Long produtoId,
            String produtoNome,
            Integer quantidadeNecessaria,
            Integer quantidadeDisponivel
    ) {
    }
}
