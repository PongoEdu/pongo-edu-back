package com.pongo.pongoedu.application.dto.request;

import jakarta.validation.constraints.Size;

public record AtendimentoRequest(
        @Size(max = 255) String observacao
) {
}
