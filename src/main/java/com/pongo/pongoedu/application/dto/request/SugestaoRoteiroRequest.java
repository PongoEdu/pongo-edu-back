package com.pongo.pongoedu.application.dto.request;

import com.pongo.pongoedu.domain.enums.NivelEnsino;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SugestaoRoteiroRequest(
        @NotBlank @Size(max = 150) String tema,
        @NotNull NivelEnsino nivelEnsino,
        @Size(max = 80) String disciplina,
        String objetivo
) {
}
