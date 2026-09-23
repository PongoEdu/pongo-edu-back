package com.pongo.pongoedu.application.dto.request;

import jakarta.validation.constraints.Size;

public record DevolucaoRequest(
        @Size(max = 255) String ocorrencia
) {
}
