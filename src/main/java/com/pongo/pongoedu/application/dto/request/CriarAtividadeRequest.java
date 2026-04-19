package com.pongo.pongoedu.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriarAtividadeRequest {
    private String titulo;
    private String descricao;
    private Long turmaId;
    private Long roteiroId;
    private Integer xpRecompensa;
    private LocalDateTime dataVencimento;
}
