package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.Recebimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RecebimentoRepository extends JpaRepository<Recebimento, Long> {

    List<Recebimento> findAllByOrderByDataRecebimentoDesc();

    List<Recebimento> findByDataRecebimentoBetween(LocalDate inicio, LocalDate fim);
}
