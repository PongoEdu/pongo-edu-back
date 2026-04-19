package com.pongo.pongoedu.infrastructure.persistence.adapter;

import com.pongo.pongoedu.domain.entities.Roteiro;
import com.pongo.pongoedu.domain.repository.RoteiroRepository;
import com.pongo.pongoedu.infrastructure.persistence.jpa.RoteiroJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoteiroRepositoryImpl implements RoteiroRepository {
    private final RoteiroJpaRepository roteiroJpaRepository;

    @Override
    public Roteiro salvar(Roteiro roteiro) {
        return roteiroJpaRepository.save(roteiro);
    }

    @Override
    public Optional<Roteiro> buscarPorId(Long id) {
        return roteiroJpaRepository.findById(id);
    }

    @Override
    public List<Roteiro> buscarTodos() {
        return roteiroJpaRepository.findAll();
    }

    @Override
    public List<Roteiro> buscarPorProfessorId(Long professorId) {
        return roteiroJpaRepository.findByProfessorId(professorId);
    }

    @Override
    public void deletar(Long id) {
        roteiroJpaRepository.deleteById(id);
    }
}


