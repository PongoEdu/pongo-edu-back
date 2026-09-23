package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.LoteRequest;
import com.pongo.pongoedu.application.dto.response.LoteResponse;
import com.pongo.pongoedu.application.service.LoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Suporte ao RF06 (recebimento) e ao RF05 (controle de validade): gestao de lotes.
 */
@RestController
@RequestMapping("/lotes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
public class LoteController {

    private final LoteService loteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResponse criar(@Valid @RequestBody LoteRequest request) {
        return loteService.criar(request);
    }

    @GetMapping
    public List<LoteResponse> listar() {
        return loteService.listar();
    }

    @GetMapping("/vencidos")
    public List<LoteResponse> listarVencidos() {
        return loteService.listarVencidos();
    }

    @GetMapping("/a-vencer")
    public List<LoteResponse> listarAVencer(@RequestParam(defaultValue = "30") int dias) {
        return loteService.listarAVencer(dias);
    }

    @GetMapping("/{id}")
    public LoteResponse buscar(@PathVariable Long id) {
        return loteService.buscar(id);
    }

    @PutMapping("/{id}")
    public LoteResponse atualizar(@PathVariable Long id, @Valid @RequestBody LoteRequest request) {
        return loteService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        loteService.remover(id);
    }
}
