package com.pongo.pongoedu.application.usecase.professor;

import com.pongo.pongoedu.application.dto.response.AtividadeResponse;
import com.pongo.pongoedu.application.dto.response.DashboardProfessorResponse;
import com.pongo.pongoedu.domain.entities.Atividade;
import com.pongo.pongoedu.domain.entities.Professor;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.AtividadeRepository;
import com.pongo.pongoedu.domain.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultarDashboardProfessorUseCase {
    private final ProfessorRepository professorRepository;
    private final AtividadeRepository atividadeRepository;

    public DashboardProfessorResponse executar(Long professorId) {
        Professor professor = professorRepository.buscarPorId(professorId)
            .orElseThrow(() -> ResourceNotFoundException.of("Professor", professorId));

        var turmas = professor.getTurmas() != null ? professor.getTurmas()
            .stream()
            .map(t -> new DashboardProfessorResponse.TurmaResponse(
                t.getId(),
                t.getNome(),
                t.getAlunos() != null ? t.getAlunos().size() : 0
            ))
            .collect(Collectors.toList())
            : null;

        var atividades = atividadeRepository.buscarPorProfessorId(professorId)
            .stream()
            .map(this::mapearParaAtividadeResponse)
            .collect(Collectors.toList());

        return new DashboardProfessorResponse(
            professor.getId(),
            professor.getUsuario().getNome(),
            turmas,
            atividades
        );
    }

    private AtividadeResponse mapearParaAtividadeResponse(Atividade atividade) {
        return new AtividadeResponse(
                atividade.getId(),
                atividade.getTitulo(),
                atividade.getDescricao(),
                atividade.getXpRecompensa(),
                atividade.getStatus(),
                atividade.getDataEntrega()
        );
    }
}
