package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.RecebimentoItem;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecebimentoItemResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        String loteCodigo,
        LocalDate dataValidade,
        Integer quantidade,
        BigDecimal valorUnitario
) {
    public static RecebimentoItemResponse de(RecebimentoItem item) {
        return new RecebimentoItemResponse(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getLote().getCodigo(),
                item.getLote().getDataValidade(),
                item.getQuantidade(),
                item.getValorUnitario()
        );
    }
}
