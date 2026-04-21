package com.pongo.pongoedu.infrastructure.persistence.adapter;

import com.pongo.pongoedu.domain.entities.Experimento;
import com.pongo.pongoedu.domain.enums.EfeitoVisual;
import com.pongo.pongoedu.domain.repository.ExperimentoRepository;
import com.pongo.pongoedu.infrastructure.persistence.jpa.ExperimentoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExperimentoRepositoryImpl implements ExperimentoRepository {
    private final ExperimentoJpaRepository jpaRepo;

    @Override
    public Experimento salvar(Experimento experimento) {
        return jpaRepo.save(experimento);
    }

    @Override
    public Optional<Experimento> buscarPorId(Long id) {
        return jpaRepo.findById(id);
    }

    @Override
    public List<Experimento> buscarTodos() {
        return jpaRepo.findAll();
    }

    @Override
    public List<Experimento> buscarPorEfeito(EfeitoVisual efeito) {
        return jpaRepo.findByEfeito(efeito);
    }

    @Override
    public void deletar(Long id) {
        jpaRepo.deleteById(id);
    }
}