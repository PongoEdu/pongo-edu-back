package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.Notificacao;
import com.pongo.pongoedu.domain.enums.SituacaoNotificacao;
import com.pongo.pongoedu.domain.enums.TipoNotificacao;

import java.time.LocalDateTime;

public record NotificacaoResponse(
        Long id,
        String destinatario,
        TipoNotificacao tipo,
        String assunto,
        String mensagem,
        SituacaoNotificacao situacao,
        Integer tentativas,
        String erro,
        LocalDateTime dataGeracao,
        LocalDateTime dataEnvio
) {
    public static NotificacaoResponse de(Notificacao notificacao) {
        return new NotificacaoResponse(
                notificacao.getId(),
                notificacao.getDestinatario().getEmail(),
                notificacao.getTipo(),
                notificacao.getAssunto(),
                notificacao.getMensagem(),
                notificacao.getSituacao(),
                notificacao.getTentativas(),
                notificacao.getErro(),
                notificacao.getDataGeracao(),
                notificacao.getDataEnvio()
        );
    }
}
