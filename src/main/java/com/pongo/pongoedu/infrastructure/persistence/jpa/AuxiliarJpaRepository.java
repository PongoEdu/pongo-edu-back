package com.pongo.pongoedu.infrastructure.persistence.jpa;

import com.pongo.pongoedu.domain.entities.Auxiliar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuxiliarJpaRepository extends JpaRepository<Auxiliar, Long> {
}