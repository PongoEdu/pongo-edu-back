package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.ChecklistItemRequest;
import com.pongo.pongoedu.application.dto.response.PreparacaoResponse;
import com.pongo.pongoedu.application.service.PreparacaoService;
import com.pongo.pongoedu.application.service.SessaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RF16 - Preparar Pratica (Quadro 16).
 */
@RestController
@RequestMapping("/preparacoes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
public class PreparacaoController {

    private final PreparacaoService preparacaoService;
    private final SessaoService sessaoService;

    @PostMapping("/agendamentos/{agendamentoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PreparacaoResponse iniciar(@PathVariable Long agendamentoId) {
        return preparacaoService.iniciar(agendamentoId, sessaoService.usuarioAtual());
    }

    @GetMapping
    public List<PreparacaoResponse> listar() {
        return preparacaoService.listar();
    }

    @GetMapping("/agendamentos/{agendamentoId}")
    public PreparacaoResponse buscarPorAgendamento(@PathVariable Long agendamentoId) {
        return preparacaoService.buscarPorAgendamento(agendamentoId);
    }

    @PutMapping("/{preparacaoId}/itens/{itemId}")
    public PreparacaoResponse conferirItem(@PathVariable Long preparacaoId, @PathVariable Long itemId,
                                           @Valid @RequestBody ChecklistItemRequest request) {
        return preparacaoService.conferirItem(preparacaoId, itemId, request);
    }

    @PostMapping("/{preparacaoId}/concluir")
    public PreparacaoResponse concluir(@PathVariable Long preparacaoId) {
        return preparacaoService.concluir(preparacaoId, sessaoService.usuarioAtual());
    }
}
