package com.pongo.pongoedu.infrastructure.persistence.adapter;

import com.pongo.pongoedu.domain.entities.Atividade;
import com.pongo.pongoedu.domain.enums.StatusAtividade;
import com.pongo.pongoedu.domain.repository.AtividadeRepository;
import com.pongo.pongoedu.infrastructure.persistence.jpa.AtividadeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AtividadeRepositoryImpl implements AtividadeRepository {
    private final AtividadeJpaRepository atividadeJpaRepository;

    @Override
    public Atividade salvar(Atividade atividade) {
        return atividadeJpaRepository.save(atividade);
    }

    @Override
    public Optional<Atividade> buscarPorId(Long id) {
        return atividadeJpaRepository.findById(id);
    }

    @Override
    public List<Atividade> buscarTodas() {
        return atividadeJpaRepository.findAll();
    }

    @Override
    public List<Atividade> buscarPorTurmaId(Long turmaId) {
        return atividadeJpaRepository.findByTurmaId(turmaId);
    }

    @Override
    public List<Atividade> buscarPorProfessorId(Long professorId) {
        return atividadeJpaRepository.findByProfessorId(professorId);
    }

    @Override
    public List<Atividade> buscarPorStatus(StatusAtividade status) {
        return atividadeJpaRepository.findByStatus(status);
    }

    @Override
    public void deletar(Long id) {
        atividadeJpaRepository.deleteById(id);
    }
}


