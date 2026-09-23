package com.pongo.pongoedu.application.dto.response;

import java.time.LocalTime;
import java.util.List;

public record DisponibilidadeResponse(
        Boolean disponivel,
        String mensagem,
        List<Intervalo> ocupados,
        List<Intervalo> sugestoes
) {
    public record Intervalo(LocalTime horaInicio, LocalTime horaFim) {
    }
}
