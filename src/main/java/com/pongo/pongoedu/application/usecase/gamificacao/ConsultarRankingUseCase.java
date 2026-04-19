package com.pongo.pongoedu.application.usecase.gamificacao;

import com.pongo.pongoedu.application.dto.response.AlunoResponse;
import com.pongo.pongoedu.application.dto.response.RankingResponse;
import com.pongo.pongoedu.domain.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.IntStream;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultarRankingUseCase {

    private final AlunoRepository alunoRepository;

    public List<RankingResponse> executar(Long turmaId) {
        var alunos = alunoRepository.buscarPorTurmaOrdenadoPorXp(turmaId);

        return IntStream.range(0, alunos.size())
                .mapToObj(i -> new RankingResponse(
                        i + 1,                              // posição = índice + 1
                        alunos.get(i).getUsuario().getNome(),
                        alunos.get(i).getXpTotal()
                ))
                .collect(Collectors.toList());
    }
}