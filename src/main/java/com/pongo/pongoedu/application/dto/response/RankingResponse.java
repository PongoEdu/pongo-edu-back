package com.pongo.pongoedu.application.dto.response;

public record RankingResponse(
        Integer posicao,
        String nomeAluno,
        Integer xpTotal
) {}