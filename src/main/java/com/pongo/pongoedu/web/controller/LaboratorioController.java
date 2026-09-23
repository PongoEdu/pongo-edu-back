package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.LaboratorioRequest;
import com.pongo.pongoedu.application.dto.response.LaboratorioResponse;
import com.pongo.pongoedu.application.service.LaboratorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Suporte ao RF14/RF15: cadastro dos laboratorios disponiveis para agendamento.
 */
@RestController
@RequestMapping("/laboratorios")
@RequiredArgsConstructor
public class LaboratorioController {

    private final LaboratorioService laboratorioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public LaboratorioResponse criar(@Valid @RequestBody LaboratorioRequest request) {
        return laboratorioService.criar(request);
    }

    @GetMapping
    public List<LaboratorioResponse> listar() {
        return laboratorioService.listar();
    }

    @GetMapping("/{id}")
    public LaboratorioResponse buscar(@PathVariable Long id) {
        return laboratorioService.buscar(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public LaboratorioResponse atualizar(@PathVariable Long id, @Valid @RequestBody LaboratorioRequest request) {
        return laboratorioService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(@PathVariable Long id) {
        laboratorioService.inativar(id);
    }
}
