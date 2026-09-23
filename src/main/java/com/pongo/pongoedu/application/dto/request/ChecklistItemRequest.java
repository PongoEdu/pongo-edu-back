package com.pongo.pongoedu.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ChecklistItemRequest(
        @NotNull Boolean separado,
        @PositiveOrZero Integer quantidadeSeparada,
        Boolean pendente,
        @Size(max = 255) String observacao
) {
}
