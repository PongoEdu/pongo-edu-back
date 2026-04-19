package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entities.Atividade;
import com.pongo.pongoedu.domain.enums.StatusAtividade;

import java.util.List;
import java.util.Optional;

public interface AtividadeRepository {
    Atividade salvar(Atividade atividade);
    Optional<Atividade> buscarPorId(Long id);
    List<Atividade> buscarTodas();
    List<Atividade> buscarPorTurmaId(Long turmaId);
    List<Atividade> buscarPorProfessorId(Long professorId);
    List<Atividade> buscarPorStatus(StatusAtividade status);
    void deletar(Long id);
}
