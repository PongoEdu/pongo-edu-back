package com.pongo.pongoedu.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardProfessorResponse {
    private Long professorId;
    private String nomeProfessor;
    private List<TurmaResponse> turmas;
    private List<AtividadeResponse> atividades;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TurmaResponse {
        private Long id;
        private String nome;
        private Integer quantidadeAlunos;
    }
}
