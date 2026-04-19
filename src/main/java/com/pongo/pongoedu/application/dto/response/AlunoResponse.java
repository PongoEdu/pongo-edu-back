package com.pongo.pongoedu.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoResponse {
    private Long id;
    private String email;
    private String nome;
    private Long turmaId;
    private Integer xpTotal;
    private Integer posicaoRanking;

    public AlunoResponse(Long id, String email, String nome, Integer xpTotal) {
    }
}
