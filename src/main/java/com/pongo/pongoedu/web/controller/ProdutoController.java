package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.ProdutoRequest;
import com.pongo.pongoedu.application.dto.response.ProdutoResponse;
import com.pongo.pongoedu.application.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RF03, RF05, RF10 - Gerenciar Produtos e Consultar Catalogo (Quadro 5, Quadro 10).
 */
@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public ProdutoResponse criar(@Valid @RequestBody ProdutoRequest request) {
        return produtoService.criar(request);
    }

    @GetMapping
    public List<ProdutoResponse> listar(@RequestParam(required = false) String nome,
                                        @RequestParam(required = false) Long categoriaId,
                                        @RequestParam(required = false) Boolean somenteDisponiveis) {
        return produtoService.listar(nome, categoriaId, somenteDisponiveis);
    }

    @GetMapping("/estoque-baixo")
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public List<ProdutoResponse> listarComEstoqueBaixo() {
        return produtoService.listarComEstoqueBaixo();
    }

    @GetMapping("/proximos-vencimento")
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public List<ProdutoResponse> listarProximosDoVencimento() {
        return produtoService.listarProximosDoVencimento();
    }

    @GetMapping("/emprestaveis")
    public List<ProdutoResponse> listarEmprestaveis() {
        return produtoService.listarEmprestaveis();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscar(@PathVariable Long id) {
        return produtoService.buscar(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public ProdutoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) {
        return produtoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public ProdutoResponse remover(@PathVariable Long id) {
        return produtoService.remover(id);
    }
}
