package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.response.SolicitacaoCompraResponse;
import com.pongo.pongoedu.domain.entity.Produto;
import com.pongo.pongoedu.domain.entity.SolicitacaoCompra;
import com.pongo.pongoedu.domain.entity.Usuario;
import com.pongo.pongoedu.domain.enums.Perfil;
import com.pongo.pongoedu.domain.enums.SituacaoSolicitacaoCompra;
import com.pongo.pongoedu.domain.enums.TipoNotificacao;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.SolicitacaoCompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitacaoCompraService {

    private final SolicitacaoCompraRepository solicitacaoRepository;
    private final NotificacaoService notificacaoService;

    /**
     * RF08 - gera a solicitacao quando o produto atinge o estoque minimo.
     * Excecao 3a/3b: havendo solicitacao pendente, apenas atualiza a quantidade sugerida.
     */
    @Transactional
    public SolicitacaoCompra gerarOuAtualizar(Produto produto) {
        int sugerida = calcularQuantidadeSugerida(produto);

        var existente = solicitacaoRepository
                .findFirstByProdutoIdAndSituacao(produto.getId(), SituacaoSolicitacaoCompra.PENDENTE);

        if (existente.isPresent()) {
            var solicitacao = existente.get();
            solicitacao.setQuantidadeSugerida(sugerida);
            return solicitacaoRepository.save(solicitacao);
        }

        var solicitacao = solicitacaoRepository.save(SolicitacaoCompra.builder()
                .produto(produto)
                .quantidadeSugerida(sugerida)
                .geradaAutomaticamente(true)
                .situacao(SituacaoSolicitacaoCompra.PENDENTE)
                .build());

        notificacaoService.notificarPerfil(
                Perfil.AUXILIAR_LABORATORIO,
                TipoNotificacao.SOLICITACAO_COMPRA,
                "PongoEdu - solicitacao de compra gerada",
                "O produto " + produto.getNome() + " atingiu o estoque minimo ("
                        + produto.getQuantidadeEstoque() + "/" + produto.getEstoqueMinimo()
                        + "). Quantidade sugerida: " + sugerida + ".");

        return solicitacao;
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoCompraResponse> listar(SituacaoSolicitacaoCompra situacao) {
        var solicitacoes = situacao == null
                ? solicitacaoRepository.findAllByOrderByDataGeracaoDesc()
                : solicitacaoRepository.findBySituacaoOrderByDataGeracaoDesc(situacao);
        return solicitacoes.stream().map(SolicitacaoCompraResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public SolicitacaoCompraResponse buscar(Long id) {
        return SolicitacaoCompraResponse.de(buscarEntidade(id));
    }

    @Transactional
    public SolicitacaoCompraResponse atender(Long id, String observacao, Usuario auxiliar) {
        return SolicitacaoCompraResponse.de(
                finalizar(id, SituacaoSolicitacaoCompra.ATENDIDA, observacao, auxiliar));
    }

    @Transactional
    public SolicitacaoCompraResponse recusar(Long id, String observacao, Usuario auxiliar) {
        return SolicitacaoCompraResponse.de(
                finalizar(id, SituacaoSolicitacaoCompra.RECUSADA, observacao, auxiliar));
    }

    private SolicitacaoCompra finalizar(Long id, SituacaoSolicitacaoCompra situacao,
                                        String observacao, Usuario auxiliar) {
        var solicitacao = buscarEntidade(id);
        if (!solicitacao.estaPendente()) {
            throw new DomainException("A solicitacao ja foi encerrada com a situacao " + solicitacao.getSituacao());
        }
        solicitacao.setSituacao(situacao);
        solicitacao.setObservacao(observacao);
        solicitacao.setUsuarioAtendimento(auxiliar);
        solicitacao.setDataAtendimento(LocalDateTime.now());
        return solicitacaoRepository.save(solicitacao);
    }

    private SolicitacaoCompra buscarEntidade(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Solicitacao de compra", id));
    }

    private int calcularQuantidadeSugerida(Produto produto) {
        int alvo = Math.max(produto.getEstoqueMinimo() * 2, produto.getEstoqueMinimo() + 1);
        return Math.max(alvo - produto.getQuantidadeEstoque(), 1);
    }
}
