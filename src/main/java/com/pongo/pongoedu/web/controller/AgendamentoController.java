package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.AgendamentoRequest;
import com.pongo.pongoedu.application.dto.response.AgendamentoResponse;
import com.pongo.pongoedu.application.dto.response.DisponibilidadeResponse;
import com.pongo.pongoedu.application.service.AgendamentoService;
import com.pongo.pongoedu.application.service.SessaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * RF14, RF15 - Agendar Pratica e Acompanhar Agendamentos/Visualizar Agenda (Quadro 14, Quadro 15).
 */
@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final SessaoService sessaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROFESSOR')")
    public AgendamentoResponse agendar(@Valid @RequestBody AgendamentoRequest request) {
        return agendamentoService.agendar(request, sessaoService.usuarioAtual());
    }

    @GetMapping
    public List<AgendamentoResponse> listar() {
        return agendamentoService.listar(sessaoService.usuarioAtual());
    }

    @GetMapping("/periodo")
    public List<AgendamentoResponse> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return agendamentoService.listarPorPeriodo(inicio, fim);
    }

    @GetMapping("/disponibilidade")
    public DisponibilidadeResponse consultarDisponibilidade(
            @RequestParam Long laboratorioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return agendamentoService.consultarDisponibilidade(laboratorioId, data);
    }

    @GetMapping("/{id}")
    public AgendamentoResponse buscar(@PathVariable Long id) {
        return agendamentoService.buscar(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public AgendamentoResponse gerenciar(@PathVariable Long id, @Valid @RequestBody AgendamentoRequest request) {
        return agendamentoService.gerenciar(id, request, sessaoService.usuarioAtual());
    }

    @PostMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('PROFESSOR')")
    public AgendamentoResponse cancelar(@PathVariable Long id) {
        return agendamentoService.cancelar(id, sessaoService.usuarioAtual());
    }
}
