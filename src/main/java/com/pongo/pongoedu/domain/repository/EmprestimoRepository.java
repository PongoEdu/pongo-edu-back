package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.Emprestimo;
import com.pongo.pongoedu.domain.enums.SituacaoEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findBySituacaoOrderByDataSolicitacaoDesc(SituacaoEmprestimo situacao);

    List<Emprestimo> findByProfessorIdOrderByDataSolicitacaoDesc(Long professorId);

    List<Emprestimo> findAllByOrderByDataSolicitacaoDesc();

    @Query("""
            select e from Emprestimo e
            where e.produto.id = :produtoId
              and e.situacao in :situacoes
              and e.dataInicio <= :fim
              and e.dataFim >= :inicio
            """)
    List<Emprestimo> buscarConflitos(@Param("produtoId") Long produtoId,
                                     @Param("inicio") LocalDate inicio,
                                     @Param("fim") LocalDate fim,
                                     @Param("situacoes") Collection<SituacaoEmprestimo> situacoes);
}
