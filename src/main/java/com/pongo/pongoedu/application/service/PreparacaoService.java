package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.request.ChecklistItemRequest;
import com.pongo.pongoedu.application.dto.response.PreparacaoResponse;
import com.pongo.pongoedu.domain.entity.Agendamento;
import com.pongo.pongoedu.domain.entity.PreparacaoItem;
import com.pongo.pongoedu.domain.entity.PreparacaoPratica;
import com.pongo.pongoedu.domain.entity.Usuario;
import com.pongo.pongoedu.domain.enums.OrigemMovimentacao;
import com.pongo.pongoedu.domain.enums.SituacaoAgendamento;
import com.pongo.pongoedu.domain.enums.SituacaoPreparacao;
import com.pongo.pongoedu.domain.enums.TipoNotificacao;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.PreparacaoPraticaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PreparacaoService {

    private final PreparacaoPraticaRepository preparacaoRepository;
    private final AgendamentoService agendamentoService;
    private final EstoqueService estoqueService;
    private final NotificacaoService notificacaoService;

    /**
     * RF16 - o auxiliar inicia a preparacao, gerando o checklist a partir do roteiro vinculado.
     */
    @Transactional
    public PreparacaoResponse iniciar(Long agendamentoId, Usuario auxiliar) {
        var agendamento = agendamentoService.buscarEntidade(agendamentoId);

        if (agendamento.getRoteiro() == null) {
            throw new DomainException("O agendamento nao possui roteiro vinculado.");
        }
        if (preparacaoRepository.findByAgendamentoId(agendamentoId).isPresent()) {
            throw new DomainException("A preparacao desta pratica ja foi iniciada.");
        }

        var preparacao = PreparacaoPratica.builder()
                .agendamento(agendamento)
                .auxiliar(auxiliar)
                .situacao(SituacaoPreparacao.EM_ANDAMENTO)
                .build();

        for (var material : agendamento.getRoteiro().getMateriais()) {
            var produto = material.getProduto();
            boolean pendente = produto.getQuantidadeEstoque() < material.getQuantidadeNecessaria()
                    || produto.estaVencido();

            preparacao.adicionarItem(PreparacaoItem.builder()
                    .produto(produto)
                    .quantidadeNecessaria(material.getQuantidadeNecessaria())
                    .pendente(pendente)
                    .build());
        }

        return PreparacaoResponse.de(preparacaoRepository.save(preparacao));
    }

    @Transactional(readOnly = true)
    public PreparacaoResponse buscarPorAgendamento(Long agendamentoId) {
        return PreparacaoResponse.de(buscarEntidadePorAgendamento(agendamentoId));
    }

    @Transactional(readOnly = true)
    public List<PreparacaoResponse> listar() {
        return preparacaoRepository.findAllByOrderByDataInicioDesc().stream()
                .map(PreparacaoResponse::de)
                .toList();
    }

    /**
     * RF16 - o auxiliar confere item a item, marcando os materiais separados.
     */
    @Transactional
    public PreparacaoResponse conferirItem(Long preparacaoId, Long itemId, ChecklistItemRequest request) {
        var preparacao = buscarEntidade(preparacaoId);
        var item = preparacao.getItens().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> ResourceNotFoundException.of("Item de preparacao", itemId));

        item.setSeparado(request.separado());
        if (request.quantidadeSeparada() != null) {
            item.setQuantidadeSeparada(request.quantidadeSeparada());
        }
        if (request.pendente() != null) {
            item.setPendente(request.pendente());
        }
        item.setObservacao(request.observacao());

        // Excecao 3a/3b do RF16: material indisponivel gera pendencia e notifica o professor.
        if (Boolean.TRUE.equals(item.getPendente())) {
            notificacaoService.registrar(preparacao.getAgendamento().getProfessor(),
                    TipoNotificacao.PREPARACAO_PRATICA,
                    "PongoEdu - pendencia na preparacao da pratica",
                    "O material " + item.getProduto().getNome() + " esta indisponivel ou vencido. "
                            + "Ajuste o roteiro da pratica de " + preparacao.getAgendamento().getData() + ".");
        }

        return PreparacaoResponse.de(preparacao);
    }

    /**
     * RF16 - conclusao da preparacao: registra as saidas de estoque e marca a pratica como preparada.
     */
    @Transactional
    public PreparacaoResponse concluir(Long preparacaoId, Usuario auxiliar) {
        var preparacao = buscarEntidade(preparacaoId);

        if (SituacaoPreparacao.CONCLUIDA.equals(preparacao.getSituacao())) {
            throw new DomainException("A preparacao ja foi concluida.");
        }

        for (var item : preparacao.getItens()) {
            if (Boolean.TRUE.equals(item.getPendente())) {
                continue;
            }
            int quantidade = item.getQuantidadeSeparada() > 0 ? item.getQuantidadeSeparada() : item.getQuantidadeNecessaria();
            estoqueService.registrarSaida(item.getProduto(), quantidade, OrigemMovimentacao.PREPARACAO_PRATICA,
                    auxiliar, preparacao.getId(), "Preparacao da pratica " + preparacao.getAgendamento().getId());
        }

        preparacao.setSituacao(preparacao.possuiPendencia() ? SituacaoPreparacao.COM_PENDENCIA : SituacaoPreparacao.CONCLUIDA);
        preparacao.setDataConclusao(LocalDateTime.now());

        Agendamento agendamento = preparacao.getAgendamento();
        agendamento.setSituacao(SituacaoAgendamento.PREPARADO);

        notificacaoService.registrar(agendamento.getProfessor(), TipoNotificacao.PREPARACAO_PRATICA,
                "PongoEdu - pratica preparada",
                "A pratica de " + agendamento.getData() + " foi preparada pelo auxiliar de laboratorio.");

        return PreparacaoResponse.de(preparacaoRepository.save(preparacao));
    }

    private PreparacaoPratica buscarEntidade(Long id) {
        return preparacaoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Preparacao", id));
    }

    private PreparacaoPratica buscarEntidadePorAgendamento(Long agendamentoId) {
        return preparacaoRepository.findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> ResourceNotFoundException.of("Preparacao do agendamento", agendamentoId));
    }
}
