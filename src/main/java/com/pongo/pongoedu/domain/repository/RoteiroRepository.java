package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entities.Roteiro;

import java.util.List;
import java.util.Optional;

public interface RoteiroRepository {
    Roteiro salvar(Roteiro roteiro);
    Optional<Roteiro> buscarPorId(Long id);
    List<Roteiro> buscarTodos();
    List<Roteiro> buscarPorProfessorId(Long professorId);
    void deletar(Long id);
}
