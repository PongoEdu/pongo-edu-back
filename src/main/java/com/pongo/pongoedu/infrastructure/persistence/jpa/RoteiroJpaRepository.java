package com.pongo.pongoedu.infrastructure.persistence.jpa;

import com.pongo.pongoedu.domain.entities.Roteiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoteiroJpaRepository extends JpaRepository<Roteiro, Long> {
    List<Roteiro> findByProfessorId(Long professorId);
}

