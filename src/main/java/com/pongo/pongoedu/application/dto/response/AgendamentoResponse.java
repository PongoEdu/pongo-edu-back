package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.Agendamento;
import com.pongo.pongoedu.domain.enums.SituacaoAgendamento;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoResponse(
        Long id,
        Long professorId,
        String professorNome,
        Long laboratorioId,
        String laboratorioNome,
        Long roteiroId,
        String roteiroTitulo,
        String turma,
        LocalDate data,
        LocalTime horaInicio,
        LocalTime horaFim,
        SituacaoAgendamento situacao,
        String observacao
) {
    public static AgendamentoResponse de(Agendamento agendamento) {
        return new AgendamentoResponse(
                agendamento.getId(),
                agendamento.getProfessor().getId(),
                agendamento.getProfessor().getNome(),
                agendamento.getLaboratorio().getId(),
                agendamento.getLaboratorio().getNome(),
                agendamento.getRoteiro() != null ? agendamento.getRoteiro().getId() : null,
                agendamento.getRoteiro() != null ? agendamento.getRoteiro().getTitulo() : null,
                agendamento.getTurma(),
                agendamento.getData(),
                agendamento.getHoraInicio(),
                agendamento.getHoraFim(),
                agendamento.getSituacao(),
                agendamento.getObservacao()
        );
    }
}
