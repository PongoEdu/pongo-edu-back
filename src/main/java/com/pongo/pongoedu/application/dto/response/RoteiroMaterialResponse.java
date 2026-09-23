package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.RoteiroMaterial;

public record RoteiroMaterialResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        Integer quantidadeNecessaria,
        Integer quantidadeDisponivel,
        Boolean disponivel,
        Boolean vencido,
        String observacao
) {
    public static RoteiroMaterialResponse de(RoteiroMaterial material) {
        var produto = material.getProduto();
        boolean disponivel = produto.getQuantidadeEstoque() >= material.getQuantidadeNecessaria()
                && !produto.estaVencido();
        return new RoteiroMaterialResponse(
                material.getId(),
                produto.getId(),
                produto.getNome(),
                material.getQuantidadeNecessaria(),
                produto.getQuantidadeEstoque(),
                disponivel,
                produto.estaVencido(),
                material.getObservacao()
        );
    }
}
