package com.pongo.pongoedu.application.usecase.professor;

import com.pongo.pongoedu.application.dto.request.CriarAtividadeRequest;
import com.pongo.pongoedu.application.dto.response.AtividadeResponse;
import com.pongo.pongoedu.domain.entities.Atividade;
import com.pongo.pongoedu.domain.enums.StatusAtividade;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.AtividadeRepository;
import com.pongo.pongoedu.domain.repository.ProfessorRepository;
import com.pongo.pongoedu.domain.repository.RoteiroRepository;
import com.pongo.pongoedu.domain.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CriarAtividadeUseCase {
    private final AtividadeRepository atividadeRepository;
    private final TurmaRepository turmaRepository;
    private final RoteiroRepository roteiroRepository;
    private final ProfessorRepository professorRepository;

    public AtividadeResponse executar(Long professorId, CriarAtividadeRequest request) {
        var professor = professorRepository.buscarPorId(professorId)
            .orElseThrow(() -> ResourceNotFoundException.of("Professor", professorId));

        var turma = turmaRepository.buscarPorId(request.getTurmaId())
            .orElseThrow(() -> ResourceNotFoundException.of("Turma", request.getTurmaId()));

        var roteiro = request.getRoteiroId() != null 
            ? roteiroRepository.buscarPorId(request.getRoteiroId())
                .orElseThrow(() -> ResourceNotFoundException.of("Roteiro", request.getRoteiroId()))
            : null;

        Atividade atividade = new Atividade();
        atividade.setTitulo(request.getTitulo());
        atividade.setDescricao(request.getDescricao());
        atividade.setProfessor(professor);
        atividade.setTurma(turma);
        atividade.setRoteiro(roteiro);
        atividade.setXpRecompensa(request.getXpRecompensa());
        atividade.setStatus(StatusAtividade.NAO_INICIADA);
        atividade.setDataVencimento(request.getDataVencimento());
        atividade.setDataCriacao(LocalDateTime.now());

        atividade = atividadeRepository.salvar(atividade);

        return mapearParaResponse(atividade);
    }

    private AtividadeResponse mapearParaResponse(Atividade atividade) {
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
