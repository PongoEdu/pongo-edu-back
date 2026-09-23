package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.AjusteEstoqueRequest;
import com.pongo.pongoedu.application.dto.response.MovimentacaoResponse;
import com.pongo.pongoedu.application.service.EstoqueService;
import com.pongo.pongoedu.application.service.SessaoService;
import com.pongo.pongoedu.domain.enums.TipoMovimentacao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * RF07 - Consultar Movimentacoes (Quadro 7).
 */
@RestController
@RequestMapping("/movimentacoes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
public class MovimentacaoController {

    private final EstoqueService estoqueService;
    private final SessaoService sessaoService;

    @GetMapping
    public List<MovimentacaoResponse> consultar(@RequestParam(required = false) Long produtoId,
                                                @RequestParam(required = false) TipoMovimentacao tipo,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return estoqueService.consultar(produtoId, tipo, inicio, fim);
    }

    @PostMapping("/ajuste")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoResponse ajustarManualmente(@Valid @RequestBody AjusteEstoqueRequest request) {
        return estoqueService.registrarAjusteManual(request, sessaoService.usuarioAtual());
    }
}
