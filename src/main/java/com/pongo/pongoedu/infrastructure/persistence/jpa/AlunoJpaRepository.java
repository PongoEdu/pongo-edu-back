package com.pongo.pongoedu.infrastructure.persistence.jpa;

import com.pongo.pongoedu.domain.entities.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface AlunoJpaRepository extends JpaRepository<Aluno, Long> {

    @Query("SELECT a FROM Aluno a JOIN a.turmas t WHERE t.id = :turmaId")
    List<Aluno> findByTurmaId(@Param("turmaId") Long turmaId);

    @Query("SELECT a FROM Aluno a JOIN a.turmas t WHERE t.id = :turmaId ORDER BY a.xpTotal DESC")
    List<Aluno> findByTurmaOrdenadoPorXp(@Param("turmaId") Long turmaId);
}