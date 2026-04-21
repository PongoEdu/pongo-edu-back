package com.pongo.pongoedu.application.usecase.experimento;

import com.pongo.pongoedu.application.dto.response.ExperimentoDisponibilidadeResponse;
import com.pongo.pongoedu.domain.enums.EfeitoVisual;
import com.pongo.pongoedu.domain.repository.ExperimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarExperimentosComDisponibilidadeUseCase {
    private final ExperimentoRepository experimentoRepo;

    public List<ExperimentoDisponibilidadeResponse> executar(EfeitoVisual efeito) {
        var experimentos = efeito != null
                ? experimentoRepo.buscarPorEfeito(efeito)
                : experimentoRepo.buscarTodos();

        return experimentos.stream().map(exp -> {
            var reagentes = exp.getReagentes();

            var faltando = reagentes.stream()
                    .filter(r -> r.getQuantidade() <= 0)
                    .map(r -> r.getNome())
                    .toList();

            return new ExperimentoDisponibilidadeResponse(
                    exp.getId(),
                    exp.getNome(),
                    exp.getDescricao(),
                    exp.getEfeito(),
                    exp.getNivelSeguranca(),
                    exp.getTempoEstimado(),
                    exp.getSerie(),
                    reagentes.size(),
                    reagentes.size() - faltando.size(),
                    faltando,
                    faltando.isEmpty()
            );
        }).toList();
    }
}