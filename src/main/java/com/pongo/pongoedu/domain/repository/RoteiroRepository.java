package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.Roteiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoteiroRepository extends JpaRepository<Roteiro, Long> {

    List<Roteiro> findByProfessorIdOrderByCriadoEmDesc(Long professorId);

    List<Roteiro> findByPublicadoTrueOrderByTituloAsc();

    boolean existsByMateriaisProdutoId(Long produtoId);
}
