package com.pongo.pongoedu.infrastructure.persistence.adapter;

import com.pongo.pongoedu.domain.entities.Reagente;
import com.pongo.pongoedu.domain.repository.ReagenteRepository;
import com.pongo.pongoedu.infrastructure.persistence.jpa.ReagenteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReagenteRepositoryImpl implements ReagenteRepository {
    private final ReagenteJpaRepository jpaRepo;

    @Override
    public Reagente salvar(Reagente reagente) {
        return jpaRepo.save(reagente);
    }

    @Override
    public Optional<Reagente> buscarPorId(Long id) {
        return jpaRepo.findById(id);
    }

    @Override
    public List<Reagente> buscarTodos() {
        return jpaRepo.findAll();
    }

    @Override
    public void deletar(Long id) {
        jpaRepo.deleteById(id);
    }
}