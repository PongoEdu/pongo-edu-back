package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.request.ProdutoRequest;
import com.pongo.pongoedu.application.dto.response.ProdutoResponse;
import com.pongo.pongoedu.domain.entity.Produto;
import com.pongo.pongoedu.domain.exception.ConflitoException;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.MovimentacaoEstoqueRepository;
import com.pongo.pongoedu.domain.repository.ProdutoRepository;
import com.pongo.pongoedu.domain.repository.RoteiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private static final int DIAS_PROXIMO_VENCIMENTO = 30;

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final RoteiroRepository roteiroRepository;
    private final CategoriaService categoriaService;
    private final LoteService loteService;
    private final SolicitacaoCompraService solicitacaoCompraService;

    @Transactional
    public ProdutoResponse criar(ProdutoRequest request) {
        if (produtoRepository.existsByCodigoIgnoreCase(request.codigo())) {
            throw new ConflitoException("Ja existe um produto com o codigo " + request.codigo());
        }
        var produto = Produto.builder()
                .codigo(request.codigo())
                .nome(request.nome())
                .descricao(request.descricao())
                .categoria(categoriaService.buscarEntidade(request.categoriaId()))
                .lote(loteService.buscarEntidade(request.loteId()))
                .unidadeMedida(request.unidadeMedida())
                .quantidadeEstoque(request.quantidadeEstoque())
                .estoqueMinimo(request.estoqueMinimo())
                .localizacao(request.localizacao())
                .ativo(true)
                .build();

        produtoRepository.save(produto);
        avaliarEstoqueMinimo(produto);
        return ProdutoResponse.de(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listar(String nome, Long categoriaId, Boolean somenteDisponiveis) {
        List<Produto> produtos;
        if (nome != null && !nome.isBlank()) {
            produtos = produtoRepository.findByAtivoTrueAndNomeContainingIgnoreCase(nome);
        } else if (categoriaId != null) {
            produtos = produtoRepository.findByAtivoTrueAndCategoriaId(categoriaId);
        } else {
            produtos = produtoRepository.findByAtivoTrue();
        }

        if (Boolean.TRUE.equals(somenteDisponiveis)) {
            produtos = produtos.stream()
                    .filter(produto -> produto.getQuantidadeEstoque() > 0 && !produto.estaVencido())
                    .toList();
        }
        return produtos.stream().map(ProdutoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscar(Long id) {
        return ProdutoResponse.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarComEstoqueBaixo() {
        return produtoRepository.buscarComEstoqueBaixo().stream().map(ProdutoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarProximosDoVencimento() {
        return produtoRepository
                .buscarProximosDoVencimento(LocalDate.now(), LocalDate.now().plusDays(DIAS_PROXIMO_VENCIMENTO))
                .stream()
                .map(ProdutoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarEmprestaveis() {
        return produtoRepository.findByAtivoTrueAndCategoriaPermiteEmprestimoTrue().stream()
                .map(ProdutoResponse::de)
                .toList();
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        var produto = buscarEntidade(id);
        boolean codigoMudou = !produto.getCodigo().equalsIgnoreCase(request.codigo());
        if (codigoMudou && produtoRepository.existsByCodigoIgnoreCase(request.codigo())) {
            throw new ConflitoException("Ja existe um produto com o codigo " + request.codigo());
        }

        produto.setCodigo(request.codigo());
        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setCategoria(categoriaService.buscarEntidade(request.categoriaId()));
        produto.setLote(loteService.buscarEntidade(request.loteId()));
        produto.setUnidadeMedida(request.unidadeMedida());
        produto.setQuantidadeEstoque(request.quantidadeEstoque());
        produto.setEstoqueMinimo(request.estoqueMinimo());
        produto.setLocalizacao(request.localizacao());

        produtoRepository.save(produto);
        avaliarEstoqueMinimo(produto);
        return ProdutoResponse.de(produto);
    }

    /**
     * Excecao 4c/4d do RF03: produto com historico nao e excluido, apenas inativado.
     */
    @Transactional
    public ProdutoResponse remover(Long id) {
        var produto = buscarEntidade(id);
        boolean possuiVinculo = movimentacaoRepository.existsByProdutoId(id)
                || roteiroRepository.existsByMateriaisProdutoId(id);

        if (!possuiVinculo) {
            produtoRepository.delete(produto);
            return ProdutoResponse.de(produto);
        }

        if (Boolean.FALSE.equals(produto.getAtivo())) {
            throw new DomainException("O produto ja esta inativo.");
        }
        produto.setAtivo(false);
        return ProdutoResponse.de(produtoRepository.save(produto));
    }

    public Produto buscarEntidade(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Produto", id));
    }

    private void avaliarEstoqueMinimo(Produto produto) {
        if (produto.atingiuEstoqueMinimo()) {
            solicitacaoCompraService.gerarOuAtualizar(produto);
        }
    }
}
