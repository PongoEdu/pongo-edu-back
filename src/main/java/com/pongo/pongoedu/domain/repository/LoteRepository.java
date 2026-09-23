package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoteRepository extends JpaRepository<Lote, Long> {

    Optional<Lote> findByCodigoIgnoreCase(String codigo);

    boolean existsByCodigoIgnoreCase(String codigo);

    List<Lote> findByDataValidadeBefore(LocalDate data);

    List<Lote> findByDataValidadeBetween(LocalDate inicio, LocalDate fim);
}
