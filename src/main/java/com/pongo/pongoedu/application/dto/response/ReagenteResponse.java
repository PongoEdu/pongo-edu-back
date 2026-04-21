package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.enums.StatusEstoque;
import com.pongo.pongoedu.domain.enums.TipoMaterial;

public record ReagenteResponse(
        Long id,
        String nome,
        String formula,
        String descricao,
        Integer quantidade,
        Integer quantidadeMinima,
        String unidade,
        String localizacao,
        TipoMaterial tipo,
        StatusEstoque status
) {}