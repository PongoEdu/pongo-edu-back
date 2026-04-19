package com.pongo.pongoedu.infrastructure.persistence.jpa;

import com.pongo.pongoedu.domain.entities.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorJpaRepository extends JpaRepository<Professor, Long> {
}

