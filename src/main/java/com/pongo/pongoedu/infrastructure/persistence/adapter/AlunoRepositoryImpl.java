package com.pongo.pongoedu.infrastructure.persistence.adapter;

import com.pongo.pongoedu.domain.entities.Aluno;
import com.pongo.pongoedu.domain.repository.AlunoRepository;
import com.pongo.pongoedu.infrastructure.persistence.jpa.AlunoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AlunoRepositoryImpl implements AlunoRepository {
    private final AlunoJpaRepository alunoJpaRepository;

    @Override
    public Aluno salvar(Aluno aluno) {
        return alunoJpaRepository.save(aluno);
    }

    @Override
    public Optional<Aluno> buscarPorId(Long id) {
        return alunoJpaRepository.findById(id);
    }

    @Override
    public List<Aluno> buscarTodos() {
        return alunoJpaRepository.findAll();
    }

    @Override
    public List<Aluno> buscarPorTurmaId(Long turmaId) {
        return alunoJpaRepository.findByTurmaId(turmaId);
    }

    @Override
    public void deletar(Long id) {
        alunoJpaRepository.deleteById(id);
    }

    @Override
    public List<Aluno> buscarPorTurmaOrdenadoPorXp(Long turmaId) {
        return alunoJpaRepository.findByTurmaOrdenadoPorXp(turmaId);
    }
}

