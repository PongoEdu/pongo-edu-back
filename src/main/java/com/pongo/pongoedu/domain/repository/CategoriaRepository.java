package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    List<Categoria> findByAtivoTrue();

    List<Categoria> findByPermiteEmprestimoTrue();
}
