package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.Roteiro;
import com.pongo.pongoedu.domain.enums.NivelEnsino;
import com.pongo.pongoedu.domain.enums.NivelSeguranca;

import java.time.LocalDateTime;
import java.util.List;

public record RoteiroResponse(
        Long id,
        String titulo,
        String objetivo,
        String procedimento,
        String tema,
        String disciplina,
        NivelEnsino nivelEnsino,
        NivelSeguranca nivelSeguranca,
        Integer tempoEstimadoMinutos,
        Long professorId,
        String professorNome,
        Boolean geradoPorIa,
        Boolean publicado,
        Boolean possuiPendenciaMaterial,
        LocalDateTime criadoEm,
        List<RoteiroMaterialResponse> materiais
) {
    public static RoteiroResponse de(Roteiro roteiro) {
        return new RoteiroResponse(
                roteiro.getId(),
                roteiro.getTitulo(),
                roteiro.getObjetivo(),
                roteiro.getProcedimento(),
                roteiro.getTema(),
                roteiro.getDisciplina(),
                roteiro.getNivelEnsino(),
                roteiro.getNivelSeguranca(),
                roteiro.getTempoEstimadoMinutos(),
                roteiro.getProfessor().getId(),
                roteiro.getProfessor().getNome(),
                roteiro.getGeradoPorIa(),
                roteiro.getPublicado(),
                roteiro.getPossuiPendenciaMaterial(),
                roteiro.getCriadoEm(),
                roteiro.getMateriais().stream().map(RoteiroMaterialResponse::de).toList()
        );
    }
}
