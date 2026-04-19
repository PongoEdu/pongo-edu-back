package com.pongo.pongoedu.application.usecase.aluno;

import com.pongo.pongoedu.application.dto.response.AtividadeResponse;
import com.pongo.pongoedu.application.dto.response.DashboardAlunoResponse;
import com.pongo.pongoedu.domain.entities.Aluno;
import com.pongo.pongoedu.domain.entities.Atividade;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.AlunoRepository;
import com.pongo.pongoedu.domain.repository.AtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultarDashboardAlunoUseCase {
    private final AlunoRepository alunoRepository;
    private final AtividadeRepository atividadeRepository;

    public DashboardAlunoResponse executar(Long alunoId) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> ResourceNotFoundException.of("Aluno", alunoId));

        var atividades = aluno.getTurmas().stream()
                .flatMap(turma ->
                        atividadeRepository.buscarPorTurmaId(turma.getId()).stream())
                .map(this::mapearParaAtividadeResponse)
                .collect(Collectors.toList());

        return new DashboardAlunoResponse(
                aluno.getId(),
                aluno.getUsuario().getNome(),
                aluno.getXpTotal(),
                aluno.getOfensivaDias(),
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