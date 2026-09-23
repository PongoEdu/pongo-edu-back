package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.MovimentacaoEstoque;
import com.pongo.pongoedu.domain.enums.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findTop50ByOrderByDataMovimentacaoDesc();

    boolean existsByProdutoId(Long produtoId);

    boolean existsByUsuarioId(Long usuarioId);

    List<MovimentacaoEstoque> findByTipoAndDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(
            TipoMovimentacao tipo, LocalDateTime inicio, LocalDateTime fim);

    @Query("""
            select m from MovimentacaoEstoque m
            where (:produtoId is null or m.produto.id = :produtoId)
              and (:tipo is null or m.tipo = :tipo)
              and (:inicio is null or m.dataMovimentacao >= :inicio)
              and (:fim is null or m.dataMovimentacao <= :fim)
            order by m.dataMovimentacao desc
            """)
    List<MovimentacaoEstoque> filtrar(@Param("produtoId") Long produtoId,
                                      @Param("tipo") TipoMovimentacao tipo,
                                      @Param("inicio") LocalDateTime inicio,
                                      @Param("fim") LocalDateTime fim);
}
