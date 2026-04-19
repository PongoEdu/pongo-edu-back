package com.pongo.pongoedu.application.dto.response;

import java.util.List;

public record DashboardAlunoResponse(
        Long alunoId,
        String nomeAluno,
        Integer xpTotal,
        Integer ofensivaDias,
        List<AtividadeResponse> atividades
) {}