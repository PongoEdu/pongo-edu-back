package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.Laboratorio;

public record LaboratorioResponse(
        Long id,
        String nome,
        String descricao,
        Integer capacidade,
        Boolean ativo
) {
    public static LaboratorioResponse de(Laboratorio laboratorio) {
        return new LaboratorioResponse(
                laboratorio.getId(),
                laboratorio.getNome(),
                laboratorio.getDescricao(),
                laboratorio.getCapacidade(),
                laboratorio.getAtivo()
        );
    }
}
