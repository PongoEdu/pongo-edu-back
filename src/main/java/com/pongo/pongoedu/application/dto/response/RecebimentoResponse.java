package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.Recebimento;
import com.pongo.pongoedu.domain.enums.TipoEntrada;

import java.time.LocalDate;
import java.util.List;

public record RecebimentoResponse(
        Long id,
        TipoEntrada tipoEntrada,
        String numeroNotaFiscal,
        String fornecedor,
        LocalDate dataRecebimento,
        String auxiliarNome,
        String observacao,
        List<RecebimentoItemResponse> itens
) {
    public static RecebimentoResponse de(Recebimento recebimento) {
        return new RecebimentoResponse(
                recebimento.getId(),
                recebimento.getTipoEntrada(),
                recebimento.getNumeroNotaFiscal(),
                recebimento.getFornecedor(),
                recebimento.getDataRecebimento(),
                recebimento.getAuxiliar().getNome(),
                recebimento.getObservacao(),
                recebimento.getItens().stream().map(RecebimentoItemResponse::de).toList()
        );
    }
}
