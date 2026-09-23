package com.pongo.pongoedu.application.port;

import com.pongo.pongoedu.domain.entity.Notificacao;

public interface NotificadorPort {

    void enviar(Notificacao notificacao);
}
