package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entities.Turma;

import java.util.List;
import java.util.Optional;

public interface TurmaRepository {
    Turma salvar(Turma turma);
    Optional<Turma> buscarPorId(Long id);
    List<Turma> buscarTodas();
    List<Turma> buscarPorProfessorId(Long professorId);
    void deletar(Long id);
}
