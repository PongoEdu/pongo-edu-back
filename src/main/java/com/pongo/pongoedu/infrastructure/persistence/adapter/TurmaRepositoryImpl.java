package com.pongo.pongoedu.infrastructure.persistence.adapter;

import com.pongo.pongoedu.domain.entities.Turma;
import com.pongo.pongoedu.domain.repository.TurmaRepository;
import com.pongo.pongoedu.infrastructure.persistence.jpa.TurmaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TurmaRepositoryImpl implements TurmaRepository {
    private final TurmaJpaRepository turmaJpaRepository;

    @Override
    public Turma salvar(Turma turma) {
        return turmaJpaRepository.save(turma);
    }

    @Override
    public Optional<Turma> buscarPorId(Long id) {
        return turmaJpaRepository.findById(id);
    }

    @Override
    public List<Turma> buscarTodas() {
        return turmaJpaRepository.findAll();
    }

    @Override
    public List<Turma> buscarPorProfessorId(Long professorId) {
        return turmaJpaRepository.findByProfessorId(professorId);
    }

    @Override
    public void deletar(Long id) {
        turmaJpaRepository.deleteById(id);
    }
}

