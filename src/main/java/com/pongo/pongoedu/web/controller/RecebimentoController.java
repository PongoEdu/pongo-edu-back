package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.RecebimentoRequest;
import com.pongo.pongoedu.application.dto.response.RecebimentoResponse;
import com.pongo.pongoedu.application.service.RecebimentoService;
import com.pongo.pongoedu.application.service.SessaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RF06 - Registrar Recebimento (Quadro 6).
 */
@RestController
@RequestMapping("/recebimentos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
public class RecebimentoController {

    private final RecebimentoService recebimentoService;
    private final SessaoService sessaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecebimentoResponse registrar(@Valid @RequestBody RecebimentoRequest request) {
        return recebimentoService.registrar(request, sessaoService.usuarioAtual());
    }

    @GetMapping
    public List<RecebimentoResponse> listar() {
        return recebimentoService.listar();
    }

    @GetMapping("/{id}")
    public RecebimentoResponse buscar(@PathVariable Long id) {
        return recebimentoService.buscar(id);
    }
}
