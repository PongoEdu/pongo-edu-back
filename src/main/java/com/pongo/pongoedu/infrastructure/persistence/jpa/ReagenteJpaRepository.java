package com.pongo.pongoedu.infrastructure.persistence.jpa;

import com.pongo.pongoedu.domain.entities.Reagente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReagenteJpaRepository extends JpaRepository<Reagente, Long> {
}