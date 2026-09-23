package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.PreparacaoPratica;
import com.pongo.pongoedu.domain.enums.SituacaoPreparacao;

import java.time.LocalDateTime;
import java.util.List;

public record PreparacaoResponse(
        Long id,
        Long agendamentoId,
        String auxiliarNome,
        SituacaoPreparacao situacao,
        LocalDateTime dataInicio,
        LocalDateTime dataConclusao,
        String observacao,
        List<PreparacaoItemResponse> itens
) {
    public static PreparacaoResponse de(PreparacaoPratica preparacao) {
        return new PreparacaoResponse(
                preparacao.getId(),
                preparacao.getAgendamento().getId(),
                preparacao.getAuxiliar().getNome(),
                preparacao.getSituacao(),
                preparacao.getDataInicio(),
                preparacao.getDataConclusao(),
                preparacao.getObservacao(),
                preparacao.getItens().stream().map(PreparacaoItemResponse::de).toList()
        );
    }
}
