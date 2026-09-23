package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {

    List<Laboratorio> findByAtivoTrue();
}
