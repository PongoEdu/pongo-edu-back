package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.request.RecebimentoRequest;
import com.pongo.pongoedu.application.dto.response.RecebimentoResponse;
import com.pongo.pongoedu.domain.entity.Recebimento;
import com.pongo.pongoedu.domain.entity.RecebimentoItem;
import com.pongo.pongoedu.domain.entity.Usuario;
import com.pongo.pongoedu.domain.enums.OrigemMovimentacao;
import com.pongo.pongoedu.domain.enums.TipoEntrada;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.RecebimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecebimentoService {

    private final RecebimentoRepository recebimentoRepository;
    private final ProdutoService produtoService;
    private final LoteService loteService;
    private final EstoqueService estoqueService;

    /**
     * RF06 - registra a entrada por codigo do sistema ou por nota fiscal e atualiza o estoque.
     */
    @Transactional
    public RecebimentoResponse registrar(RecebimentoRequest request, Usuario auxiliar) {
        if (TipoEntrada.NOTA_FISCAL.equals(request.tipoEntrada())
                && (request.numeroNotaFiscal() == null || request.numeroNotaFiscal().isBlank())) {
            throw new DomainException("Informe o numero da nota fiscal para a entrada por nota fiscal.");
        }

        var recebimento = Recebimento.builder()
                .tipoEntrada(request.tipoEntrada())
                .numeroNotaFiscal(request.numeroNotaFiscal())
                .fornecedor(request.fornecedor())
                .dataRecebimento(request.dataRecebimento())
                .auxiliar(auxiliar)
                .observacao(request.observacao())
                .build();

        for (var itemRequest : request.itens()) {
            var produto = produtoService.buscarEntidade(itemRequest.produtoId());
            var lote = loteService.buscarEntidade(itemRequest.loteId());

            // Excecao 3c/3d do RF06: item vencido exige confirmacao explicita.
            if (lote.estaVencido() && !Boolean.TRUE.equals(itemRequest.confirmarVencido())) {
                throw new DomainException("O lote " + lote.getCodigo() + " do produto " + produto.getNome()
                        + " esta vencido (validade " + lote.getDataValidade()
                        + "). Confirme explicitamente para registrar a entrada.");
            }

            recebimento.adicionarItem(RecebimentoItem.builder()
                    .produto(produto)
                    .lote(lote)
                    .quantidade(itemRequest.quantidade())
                    .valorUnitario(itemRequest.valorUnitario())
                    .vencidoConfirmado(lote.estaVencido())
                    .build());
        }

        recebimentoRepository.save(recebimento);

        for (var item : recebimento.getItens()) {
            var produto = item.getProduto();
            produto.setLote(item.getLote());
            estoqueService.registrarEntrada(produto, item.getQuantidade(), OrigemMovimentacao.RECEBIMENTO,
                    auxiliar, recebimento.getId(), "Recebimento " + recebimento.getId());
        }

        return RecebimentoResponse.de(recebimento);
    }

    @Transactional(readOnly = true)
    public List<RecebimentoResponse> listar() {
        return recebimentoRepository.findAllByOrderByDataRecebimentoDesc().stream()
                .map(RecebimentoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public RecebimentoResponse buscar(Long id) {
        return RecebimentoResponse.de(recebimentoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Recebimento", id)));
    }
}
