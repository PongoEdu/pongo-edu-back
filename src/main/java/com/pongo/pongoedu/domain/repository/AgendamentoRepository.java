package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.Agendamento;
import com.pongo.pongoedu.domain.enums.SituacaoAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByProfessorIdOrderByDataDescHoraInicioDesc(Long professorId);

    List<Agendamento> findByDataBetweenOrderByDataAscHoraInicioAsc(LocalDate inicio, LocalDate fim);

    List<Agendamento> findAllByOrderByDataDescHoraInicioDesc();

    boolean existsByRoteiroId(Long roteiroId);

    boolean existsByProfessorId(Long professorId);

    List<Agendamento> findByDataAndSituacao(LocalDate data, SituacaoAgendamento situacao);

    @Query("""
            select a from Agendamento a
            where a.laboratorio.id = :laboratorioId
              and a.data = :data
              and a.situacao <> :situacaoIgnorada
              and (:agendamentoId is null or a.id <> :agendamentoId)
              and a.horaInicio < :horaFim
              and a.horaFim > :horaInicio
            """)
    List<Agendamento> buscarConflitos(@Param("laboratorioId") Long laboratorioId,
                                      @Param("data") LocalDate data,
                                      @Param("horaInicio") LocalTime horaInicio,
                                      @Param("horaFim") LocalTime horaFim,
                                      @Param("situacaoIgnorada") SituacaoAgendamento situacaoIgnorada,
                                      @Param("agendamentoId") Long agendamentoId);

    @Query("""
            select a from Agendamento a
            where a.laboratorio.id = :laboratorioId
              and a.data = :data
              and a.situacao <> :situacaoIgnorada
            order by a.horaInicio asc
            """)
    List<Agendamento> buscarOcupacaoDoDia(@Param("laboratorioId") Long laboratorioId,
                                          @Param("data") LocalDate data,
                                          @Param("situacaoIgnorada") SituacaoAgendamento situacaoIgnorada);
}
