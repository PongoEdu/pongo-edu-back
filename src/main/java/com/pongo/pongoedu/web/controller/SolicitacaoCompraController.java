package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.AtendimentoRequest;
import com.pongo.pongoedu.application.dto.response.SolicitacaoCompraResponse;
import com.pongo.pongoedu.application.service.SessaoService;
import com.pongo.pongoedu.application.service.SolicitacaoCompraService;
import com.pongo.pongoedu.domain.enums.SituacaoSolicitacaoCompra;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RF08, RF09 - Gerar e Atender Solicitacao de Compra (Quadro 8, Quadro 9).
 */
@RestController
@RequestMapping("/solicitacoes-compra")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
public class SolicitacaoCompraController {

    private final SolicitacaoCompraService solicitacaoCompraService;
    private final SessaoService sessaoService;

    @GetMapping
    public List<SolicitacaoCompraResponse> listar(@RequestParam(required = false) SituacaoSolicitacaoCompra situacao) {
        return solicitacaoCompraService.listar(situacao);
    }

    @GetMapping("/{id}")
    public SolicitacaoCompraResponse buscar(@PathVariable Long id) {
        return solicitacaoCompraService.buscar(id);
    }

    @PostMapping("/{id}/atender")
    public SolicitacaoCompraResponse atender(@PathVariable Long id, @Valid @RequestBody(required = false) AtendimentoRequest request) {
        var observacao = request != null ? request.observacao() : null;
        return solicitacaoCompraService.atender(id, observacao, sessaoService.usuarioAtual());
    }

    @PostMapping("/{id}/recusar")
    public SolicitacaoCompraResponse recusar(@PathVariable Long id, @Valid @RequestBody(required = false) AtendimentoRequest request) {
        var observacao = request != null ? request.observacao() : null;
        return solicitacaoCompraService.recusar(id, observacao, sessaoService.usuarioAtual());
    }
}
