package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.SolicitacaoCompra;
import com.pongo.pongoedu.domain.enums.SituacaoSolicitacaoCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitacaoCompraRepository extends JpaRepository<SolicitacaoCompra, Long> {

    Optional<SolicitacaoCompra> findFirstByProdutoIdAndSituacao(Long produtoId, SituacaoSolicitacaoCompra situacao);

    List<SolicitacaoCompra> findBySituacaoOrderByDataGeracaoDesc(SituacaoSolicitacaoCompra situacao);

    List<SolicitacaoCompra> findAllByOrderByDataGeracaoDesc();
}
