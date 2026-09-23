package com.pongo.pongoedu.infrastructure.config;

import com.pongo.pongoedu.application.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * RF18 - dispara o ciclo semanal de novidades, os lembretes da vespera de cada
 * pratica e o reprocessamento de envios que falharam.
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class NotificacaoScheduler {

    private final NotificacaoService notificacaoService;

    @Scheduled(cron = "0 0 7 * * MON")
    public void cicloSemanal() {
        int total = notificacaoService.executarCicloSemanal();
        log.info("Ciclo semanal de novidades executado para {} usuarios.", total);
    }

    @Scheduled(cron = "0 0 18 * * *")
    public void lembretesDaVespera() {
        int total = notificacaoService.enviarLembretesDaVespera();
        log.info("Lembretes de pratica enviados: {}.", total);
    }

    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void reprocessarFalhas() {
        int total = notificacaoService.reprocessarPendentes();
        if (total > 0) {
            log.info("Reprocessadas {} notificacoes com falha.", total);
        }
    }
}
