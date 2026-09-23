package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.Produto;
import com.pongo.pongoedu.domain.enums.StatusEstoque;
import com.pongo.pongoedu.domain.enums.UnidadeMedida;

public record ProdutoResponse(
        Long id,
        String codigo,
        String nome,
        String descricao,
        CategoriaResponse categoria,
        LoteResponse lote,
        UnidadeMedida unidadeMedida,
        Integer quantidadeEstoque,
        Integer estoqueMinimo,
        StatusEstoque statusEstoque,
        String localizacao,
        Boolean vencido,
        Boolean permiteEmprestimo,
        Boolean ativo
) {
    public static ProdutoResponse de(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getCodigo(),
                produto.getNome(),
                produto.getDescricao(),
                CategoriaResponse.de(produto.getCategoria()),
                LoteResponse.de(produto.getLote()),
                produto.getUnidadeMedida(),
                produto.getQuantidadeEstoque(),
                produto.getEstoqueMinimo(),
                produto.getStatusEstoque(),
                produto.getLocalizacao(),
                produto.estaVencido(),
                produto.permiteEmprestimo(),
                produto.getAtivo()
        );
    }
}
