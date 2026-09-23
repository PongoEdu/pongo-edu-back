package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.PreparacaoItem;

public record PreparacaoItemResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        Integer quantidadeNecessaria,
        Integer quantidadeDisponivel,
        Integer quantidadeSeparada,
        Boolean separado,
        Boolean pendente,
        Boolean vencido,
        String observacao
) {
    public static PreparacaoItemResponse de(PreparacaoItem item) {
        var produto = item.getProduto();
        return new PreparacaoItemResponse(
                item.getId(),
                produto.getId(),
                produto.getNome(),
                item.getQuantidadeNecessaria(),
                produto.getQuantidadeEstoque(),
                item.getQuantidadeSeparada(),
                item.getSeparado(),
                item.getPendente(),
                produto.estaVencido(),
                item.getObservacao()
        );
    }
}
