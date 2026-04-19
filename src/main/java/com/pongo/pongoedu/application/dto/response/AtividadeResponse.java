package com.pongo.pongoedu.application.dto.response;


import com.pongo.pongoedu.domain.enums.StatusAtividade;

import java.time.LocalDate;

public record AtividadeResponse(
        Long id,
        String titulo,
        String descricao,
        Integer xpRecompensa,
        StatusAtividade status,
        LocalDate dataEntrega
) {}


