package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.RoteiroRequest;
import com.pongo.pongoedu.application.dto.request.SugestaoRoteiroRequest;
import com.pongo.pongoedu.application.dto.response.RoteiroResponse;
import com.pongo.pongoedu.application.dto.response.SugestaoRoteiroResponse;
import com.pongo.pongoedu.application.service.RoteiroService;
import com.pongo.pongoedu.application.service.SessaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RF13, RF17 - Gerenciar Roteiros de Pratica e Oferecer Apoio com IA (Quadro 13, Quadro 17).
 */
@RestController
@RequestMapping("/roteiros")
@RequiredArgsConstructor
public class RoteiroController {

    private final RoteiroService roteiroService;
    private final SessaoService sessaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROFESSOR')")
    public RoteiroResponse criar(@Valid @RequestBody RoteiroRequest request) {
        return roteiroService.criar(request, sessaoService.usuarioAtual());
    }

    @PostMapping("/sugestao-ia")
    @PreAuthorize("hasRole('PROFESSOR')")
    public SugestaoRoteiroResponse sugerir(@Valid @RequestBody SugestaoRoteiroRequest request) {
        return roteiroService.sugerir(request);
    }

    @GetMapping
    public List<RoteiroResponse> listar(@RequestParam(required = false) Boolean somentePublicados) {
        return roteiroService.listar(sessaoService.usuarioAtual(), somentePublicados);
    }

    @GetMapping("/{id}")
    public RoteiroResponse buscar(@PathVariable Long id) {
        return roteiroService.buscar(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public RoteiroResponse atualizar(@PathVariable Long id, @Valid @RequestBody RoteiroRequest request) {
        return roteiroService.atualizar(id, request, sessaoService.usuarioAtual());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        roteiroService.remover(id, sessaoService.usuarioAtual());
    }
}
