package com.pongo.pongoedu.infrastructure.notification;

import com.pongo.pongoedu.application.port.NotificadorPort;
import com.pongo.pongoedu.domain.entity.Notificacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * RF18 - implementacao de referencia do envio de notificacoes.
 * Registra o envio no log da aplicacao; pode ser substituida por um
 * adaptador real de e-mail (SMTP/SES/SendGrid) mantendo o contrato
 * de {@link NotificadorPort}.
 */
@Slf4j
@Component
public class LogNotificadorAdapter implements NotificadorPort {

    @Override
    public void enviar(Notificacao notificacao) {
        log.info("Notificacao [{}] para {}: {}",
                notificacao.getTipo(), notificacao.getDestinatario().getEmail(), notificacao.getAssunto());
    }
}
