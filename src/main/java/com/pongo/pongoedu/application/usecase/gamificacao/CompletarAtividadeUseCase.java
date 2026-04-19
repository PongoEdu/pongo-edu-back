package com.pongo.pongoedu.application.usecase.gamificacao;

import com.pongo.pongoedu.application.dto.response.AtividadeResponse;
import com.pongo.pongoedu.domain.entities.Atividade;
import com.pongo.pongoedu.domain.enums.StatusAtividade;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.AtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompletarAtividadeUseCase {
    private final AtividadeRepository atividadeRepository;

    public AtividadeResponse executar(Long atividadeId) {
        Atividade atividade = atividadeRepository.buscarPorId(atividadeId)
                .orElseThrow(() -> ResourceNotFoundException.of("Atividade", atividadeId));

        atividade.setStatus(StatusAtividade.CONCLUIDA);
        atividade = atividadeRepository.salvar(atividade);

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