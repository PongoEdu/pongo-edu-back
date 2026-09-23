package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.PreparacaoPratica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PreparacaoPraticaRepository extends JpaRepository<PreparacaoPratica, Long> {

    Optional<PreparacaoPratica> findByAgendamentoId(Long agendamentoId);

    List<PreparacaoPratica> findAllByOrderByDataInicioDesc();
}
