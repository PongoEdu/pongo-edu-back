package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entities.Reagente;
import java.util.List;
import java.util.Optional;

public interface ReagenteRepository {
    Reagente salvar(Reagente reagente);
    Optional<Reagente> buscarPorId(Long id);
    List<Reagente> buscarTodos();
    void deletar(Long id);
}