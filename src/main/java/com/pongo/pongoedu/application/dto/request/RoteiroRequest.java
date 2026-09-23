package com.pongo.pongoedu.application.dto.request;

import com.pongo.pongoedu.domain.enums.NivelEnsino;
import com.pongo.pongoedu.domain.enums.NivelSeguranca;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RoteiroRequest(
        @NotBlank @Size(max = 150) String titulo,
        String objetivo,
        String procedimento,
        @Size(max = 150) String tema,
        @Size(max = 80) String disciplina,
        NivelEnsino nivelEnsino,
        NivelSeguranca nivelSeguranca,
        @Positive Integer tempoEstimadoMinutos,
        Boolean publicado,
        Boolean geradoPorIa,
        @Valid List<RoteiroMaterialRequest> materiais
) {
}
