package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.MovimentacaoEstoque;
import com.pongo.pongoedu.domain.enums.OrigemMovimentacao;
import com.pongo.pongoedu.domain.enums.TipoMovimentacao;

import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        String loteCodigo,
        TipoMovimentacao tipo,
        OrigemMovimentacao origem,
        Integer quantidade,
        Integer quantidadeAnterior,
        Integer quantidadeResultante,
        String usuarioNome,
        String observacao,
        LocalDateTime dataMovimentacao
) {
    public static MovimentacaoResponse de(MovimentacaoEstoque movimentacao) {
        return new MovimentacaoResponse(
                movimentacao.getId(),
                movimentacao.getProduto().getId(),
                movimentacao.getProduto().getNome(),
                movimentacao.getLote().getCodigo(),
                movimentacao.getTipo(),
                movimentacao.getOrigem(),
                movimentacao.getQuantidade(),
                movimentacao.getQuantidadeAnterior(),
                movimentacao.getQuantidadeResultante(),
                movimentacao.getUsuario() != null ? movimentacao.getUsuario().getNome() : null,
                movimentacao.getObservacao(),
                movimentacao.getDataMovimentacao()
        );
    }
}
