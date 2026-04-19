package com.pongo.pongoedu.infrastructure.persistence.adapter;

import com.pongo.pongoedu.domain.entities.Professor;
import com.pongo.pongoedu.domain.repository.ProfessorRepository;
import com.pongo.pongoedu.infrastructure.persistence.jpa.ProfessorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfessorRepositoryImpl implements ProfessorRepository {
    private final ProfessorJpaRepository professorJpaRepository;

    @Override
    public Professor salvar(Professor professor) {
        return professorJpaRepository.save(professor);
    }

    @Override
    public Optional<Professor> buscarPorId(Long id) {
        return professorJpaRepository.findById(id);
    }

    @Override
    public List<Professor> buscarTodos() {
        return professorJpaRepository.findAll();
    }

    @Override
    public void deletar(Long id) {
        professorJpaRepository.deleteById(id);
    }
}

