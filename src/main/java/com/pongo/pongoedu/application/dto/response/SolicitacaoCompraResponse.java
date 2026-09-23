package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.SolicitacaoCompra;
import com.pongo.pongoedu.domain.enums.SituacaoSolicitacaoCompra;

import java.time.LocalDateTime;

public record SolicitacaoCompraResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        Integer quantidadeEstoque,
        Integer estoqueMinimo,
        Integer quantidadeSugerida,
        SituacaoSolicitacaoCompra situacao,
        Boolean geradaAutomaticamente,
        LocalDateTime dataGeracao,
        LocalDateTime dataAtendimento,
        String usuarioAtendimento,
        String observacao
) {
    public static SolicitacaoCompraResponse de(SolicitacaoCompra solicitacao) {
        return new SolicitacaoCompraResponse(
                solicitacao.getId(),
                solicitacao.getProduto().getId(),
                solicitacao.getProduto().getNome(),
                solicitacao.getProduto().getQuantidadeEstoque(),
                solicitacao.getProduto().getEstoqueMinimo(),
                solicitacao.getQuantidadeSugerida(),
                solicitacao.getSituacao(),
                solicitacao.getGeradaAutomaticamente(),
                solicitacao.getDataGeracao(),
                solicitacao.getDataAtendimento(),
                solicitacao.getUsuarioAtendimento() != null ? solicitacao.getUsuarioAtendimento().getNome() : null,
                solicitacao.getObservacao()
        );
    }
}
