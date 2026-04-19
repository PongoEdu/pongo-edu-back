package com.pongo.pongoedu.infrastructure.persistence.jpa;

import com.pongo.pongoedu.domain.entities.Atividade;
import com.pongo.pongoedu.domain.enums.StatusAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtividadeJpaRepository extends JpaRepository<Atividade, Long> {
    List<Atividade> findByTurmaId(Long turmaId);
    List<Atividade> findByProfessorId(Long professorId);
    List<Atividade> findByStatus(StatusAtividade status);
}

