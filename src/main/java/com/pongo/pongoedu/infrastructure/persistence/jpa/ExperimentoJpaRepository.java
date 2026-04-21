package com.pongo.pongoedu.infrastructure.persistence.jpa;

import com.pongo.pongoedu.domain.entities.Experimento;
import com.pongo.pongoedu.domain.enums.EfeitoVisual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperimentoJpaRepository extends JpaRepository<Experimento, Long> {
    List<Experimento> findByEfeito(EfeitoVisual efeito);
}