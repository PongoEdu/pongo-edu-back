package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.enums.TipoMaterial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialResponse {
    private Long id;
    private String nome;
    private String descricao;
    private TipoMaterial tipo;
    private Integer quantidade;
    private String localizacao;
}
