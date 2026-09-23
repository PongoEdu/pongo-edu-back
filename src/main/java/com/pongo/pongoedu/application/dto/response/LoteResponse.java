package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.Lote;

import java.time.LocalDate;

public record LoteResponse(
        Long id,
        String codigo,
        String fornecedor,
        LocalDate dataFabricacao,
        LocalDate dataValidade,
        Boolean vencido,
        String observacao
) {
    public static LoteResponse de(Lote lote) {
        return new LoteResponse(
                lote.getId(),
                lote.getCodigo(),
                lote.getFornecedor(),
                lote.getDataFabricacao(),
                lote.getDataValidade(),
                lote.estaVencido(),
                lote.getObservacao()
        );
    }
}
