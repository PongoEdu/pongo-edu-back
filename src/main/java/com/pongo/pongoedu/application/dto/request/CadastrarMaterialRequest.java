package com.pongo.pongoedu.application.dto.request;

import com.pongo.pongoedu.domain.enums.TipoMaterial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarMaterialRequest {
    private String nome;
    private String descricao;
    private TipoMaterial tipo;
    private Integer quantidade;
    private String localizacao;
}
