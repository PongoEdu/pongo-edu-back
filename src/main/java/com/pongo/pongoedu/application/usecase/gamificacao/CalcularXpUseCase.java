package com.pongo.pongoedu.application.usecase.gamificacao;

import com.pongo.pongoedu.domain.entities.Aluno;
import com.pongo.pongoedu.domain.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalcularXpUseCase {
    private final AlunoRepository alunoRepository;

    public void executar(Long alunoId, Integer xpGanho) {
        Aluno aluno = alunoRepository.buscarPorId(alunoId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        aluno.setXpTotal(aluno.getXpTotal() + xpGanho);
        alunoRepository.salvar(aluno);
    }
}
