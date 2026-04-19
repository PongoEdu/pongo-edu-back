package com.pongo.pongoedu.application.usecase.aluno;

import com.pongo.pongoedu.application.dto.response.AlunoResponse;
import com.pongo.pongoedu.domain.entities.Aluno;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.AlunoRepository;
import com.pongo.pongoedu.domain.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatricularAlunoUseCase {
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public AlunoResponse executar(Long alunoId, Long turmaId) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> ResourceNotFoundException.of("Aluno", alunoId));

        var turma = turmaRepository.buscarPorId(turmaId)
                .orElseThrow(() -> ResourceNotFoundException.of("Turma", turmaId));

        aluno.matricularEmTurma(turma);
        aluno = alunoRepository.salvar(aluno);

        return mapearParaResponse(aluno);
    }

    private AlunoResponse mapearParaResponse(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(),
                aluno.getUsuario().getEmail(),
                aluno.getUsuario().getNome(),
                aluno.getXpTotal()
        );
    }
}
