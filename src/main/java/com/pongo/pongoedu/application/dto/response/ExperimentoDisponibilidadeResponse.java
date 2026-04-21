package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.enums.EfeitoVisual;
import com.pongo.pongoedu.domain.enums.NivelSeguranca;
import java.util.List;

public record ExperimentoDisponibilidadeResponse(
        Long id,
        String nome,
        String descricao,
        EfeitoVisual efeito,
        NivelSeguranca nivelSeguranca,
        Integer tempoEstimado,
        String serie,
        Integer totalReagentes,
        Integer reagentesDisponiveis,
        List<String> reagentesFaltando,
        Boolean disponivel
) {}