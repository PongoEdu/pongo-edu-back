package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.Emprestimo;
import com.pongo.pongoedu.domain.enums.SituacaoEmprestimo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmprestimoResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        String professorNome,
        String auxiliarAtendimento,
        Integer quantidade,
        LocalDate dataInicio,
        LocalDate dataFim,
        String finalidade,
        SituacaoEmprestimo situacao,
        LocalDateTime dataSolicitacao,
        LocalDateTime dataRetirada,
        LocalDateTime dataDevolucao,
        String ocorrencia,
        String observacao,
        Boolean atrasado
) {
    public static EmprestimoResponse de(Emprestimo emprestimo) {
        return new EmprestimoResponse(
                emprestimo.getId(),
                emprestimo.getProduto().getId(),
                emprestimo.getProduto().getNome(),
                emprestimo.getProfessor().getNome(),
                emprestimo.getAuxiliarAtendimento() != null ? emprestimo.getAuxiliarAtendimento().getNome() : null,
                emprestimo.getQuantidade(),
                emprestimo.getDataInicio(),
                emprestimo.getDataFim(),
                emprestimo.getFinalidade(),
                emprestimo.getSituacao(),
                emprestimo.getDataSolicitacao(),
                emprestimo.getDataRetirada(),
                emprestimo.getDataDevolucao(),
                emprestimo.getOcorrencia(),
                emprestimo.getObservacao(),
                emprestimo.estaAtrasado()
        );
    }
}
