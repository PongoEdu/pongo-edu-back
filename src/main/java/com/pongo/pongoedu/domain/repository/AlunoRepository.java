package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entities.Aluno;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository {
    Aluno salvar(Aluno aluno);
    Optional<Aluno> buscarPorId(Long id);
    List<Aluno> buscarTodos();
    List<Aluno> buscarPorTurmaId(Long turmaId);
    List<Aluno> buscarPorTurmaOrdenadoPorXp(Long turmaId);
    void deletar(Long id);
}