package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.request.AjusteEstoqueRequest;
import com.pongo.pongoedu.application.dto.response.MovimentacaoResponse;
import com.pongo.pongoedu.domain.entity.MovimentacaoEstoque;
import com.pongo.pongoedu.domain.entity.Produto;
import com.pongo.pongoedu.domain.entity.Usuario;
import com.pongo.pongoedu.domain.enums.OrigemMovimentacao;
import com.pongo.pongoedu.domain.enums.TipoMovimentacao;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.MovimentacaoEstoqueRepository;
import com.pongo.pongoedu.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final SolicitacaoCompraService solicitacaoCompraService;

    /**
     * RF07 - registra a entrada e atualiza o saldo do produto.
     */
    @Transactional
    public MovimentacaoEstoque registrarEntrada(Produto produto, int quantidade, OrigemMovimentacao origem,
                                                Usuario usuario, Long referenciaId, String observacao) {
        int anterior = produto.getQuantidadeEstoque();
        produto.adicionarEstoque(quantidade);
        produtoRepository.save(produto);
        return gravar(produto, TipoMovimentacao.ENTRADA, origem, quantidade, anterior, usuario, referenciaId, observacao);
    }

    /**
     * RF07/RF08 - registra a saida e, ao atingir o estoque minimo, dispara a solicitacao de compra.
     */
    @Transactional
    public MovimentacaoEstoque registrarSaida(Produto produto, int quantidade, OrigemMovimentacao origem,
                                              Usuario usuario, Long referenciaId, String observacao) {
        int anterior = produto.getQuantidadeEstoque();
        produto.retirarEstoque(quantidade);
        produtoRepository.save(produto);

        var movimentacao = gravar(produto, TipoMovimentacao.SAIDA, origem, quantidade, anterior,
                usuario, referenciaId, observacao);

        if (produto.atingiuEstoqueMinimo()) {
            solicitacaoCompraService.gerarOuAtualizar(produto);
        }
        return movimentacao;
    }

    @Transactional
    public MovimentacaoResponse registrarAjusteManual(AjusteEstoqueRequest request, Usuario usuario) {
        var produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> ResourceNotFoundException.of("Produto", request.produtoId()));

        var movimentacao = TipoMovimentacao.ENTRADA.equals(request.tipo())
                ? registrarEntrada(produto, request.quantidade(), OrigemMovimentacao.AJUSTE_MANUAL,
                usuario, null, request.observacao())
                : registrarSaida(produto, request.quantidade(), OrigemMovimentacao.AJUSTE_MANUAL,
                usuario, null, request.observacao());

        return MovimentacaoResponse.de(movimentacao);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponse> consultar(Long produtoId, TipoMovimentacao tipo,
                                                LocalDate inicio, LocalDate fim) {
        LocalDateTime de = inicio != null ? inicio.atStartOfDay() : null;
        LocalDateTime ate = fim != null ? fim.atTime(LocalTime.MAX) : null;
        return movimentacaoRepository.filtrar(produtoId, tipo, de, ate).stream()
                .map(MovimentacaoResponse::de)
                .toList();
    }

    private MovimentacaoEstoque gravar(Produto produto, TipoMovimentacao tipo, OrigemMovimentacao origem,
                                       int quantidade, int anterior, Usuario usuario,
                                       Long referenciaId, String observacao) {
        return movimentacaoRepository.save(MovimentacaoEstoque.builder()
                .produto(produto)
                .lote(produto.getLote())
                .usuario(usuario)
                .tipo(tipo)
                .origem(origem)
                .quantidade(quantidade)
                .quantidadeAnterior(anterior)
                .quantidadeResultante(produto.getQuantidadeEstoque())
                .referenciaId(referenciaId)
                .observacao(observacao)
                .build());
    }
}
