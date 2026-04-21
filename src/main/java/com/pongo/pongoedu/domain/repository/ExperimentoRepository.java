package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entities.Experimento;
import com.pongo.pongoedu.domain.enums.EfeitoVisual;
import java.util.List;
import java.util.Optional;

public interface ExperimentoRepository {
    Experimento salvar(Experimento experimento);
    Optional<Experimento> buscarPorId(Long id);
    List<Experimento> buscarTodos();
    List<Experimento> buscarPorEfeito(EfeitoVisual efeito);
    void deletar(Long id);
}