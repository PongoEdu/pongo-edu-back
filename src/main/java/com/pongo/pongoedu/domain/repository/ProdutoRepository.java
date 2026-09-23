package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByCodigoIgnoreCase(String codigo);

    boolean existsByCodigoIgnoreCase(String codigo);

    List<Produto> findByAtivoTrue();

    List<Produto> findByAtivoTrueAndCategoriaId(Long categoriaId);

    List<Produto> findByAtivoTrueAndNomeContainingIgnoreCase(String nome);

    List<Produto> findByAtivoTrueAndCategoriaPermiteEmprestimoTrue();

    boolean existsByCategoriaId(Long categoriaId);

    boolean existsByLoteId(Long loteId);

    @Query("select p from Produto p where p.ativo = true and p.quantidadeEstoque <= p.estoqueMinimo")
    List<Produto> buscarComEstoqueBaixo();

    @Query("select p from Produto p where p.ativo = true and p.lote.dataValidade is not null and p.lote.dataValidade between :inicio and :fim")
    List<Produto> buscarProximosDoVencimento(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("select p from Produto p where p.ativo = true and p.lote.dataValidade is not null and p.lote.dataValidade < :data")
    List<Produto> buscarVencidos(@Param("data") LocalDate data);
}
