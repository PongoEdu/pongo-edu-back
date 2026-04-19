package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entities.Professor;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepository {
    Professor salvar(Professor professor);
    Optional<Professor> buscarPorId(Long id);
    List<Professor> buscarTodos();
    void deletar(Long id);
}

