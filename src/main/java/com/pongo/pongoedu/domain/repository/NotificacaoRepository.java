package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.Notificacao;
import com.pongo.pongoedu.domain.enums.SituacaoNotificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByDestinatarioIdOrderByDataGeracaoDesc(Long destinatarioId);

    List<Notificacao> findBySituacaoOrderByDataGeracaoAsc(SituacaoNotificacao situacao);
}
