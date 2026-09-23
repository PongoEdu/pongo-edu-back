package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.DevolucaoRequest;
import com.pongo.pongoedu.application.dto.request.EmprestimoRequest;
import com.pongo.pongoedu.application.dto.response.EmprestimoResponse;
import com.pongo.pongoedu.application.service.EmprestimoService;
import com.pongo.pongoedu.application.service.SessaoService;
import com.pongo.pongoedu.domain.enums.SituacaoEmprestimo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RF11, RF12 - Solicitar e Atender Emprestimo (Quadro 11, Quadro 12).
 */
@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final SessaoService sessaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROFESSOR')")
    public EmprestimoResponse solicitar(@Valid @RequestBody EmprestimoRequest request) {
        return emprestimoService.solicitar(request, sessaoService.usuarioAtual());
    }

    @GetMapping
    public List<EmprestimoResponse> listar(@RequestParam(required = false) SituacaoEmprestimo situacao) {
        return emprestimoService.listar(sessaoService.usuarioAtual(), situacao);
    }

    @GetMapping("/{id}")
    public EmprestimoResponse buscar(@PathVariable Long id) {
        return emprestimoService.buscar(id);
    }

    @PostMapping("/{id}/aprovar")
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public EmprestimoResponse aprovar(@PathVariable Long id, @RequestParam(required = false) String observacao) {
        return emprestimoService.aprovar(id, observacao, sessaoService.usuarioAtual());
    }

    @PostMapping("/{id}/recusar")
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public EmprestimoResponse recusar(@PathVariable Long id, @RequestParam(required = false) String observacao) {
        return emprestimoService.recusar(id, observacao, sessaoService.usuarioAtual());
    }

    @PostMapping("/{id}/retirada")
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public EmprestimoResponse registrarRetirada(@PathVariable Long id) {
        return emprestimoService.registrarRetirada(id, sessaoService.usuarioAtual());
    }

    @PostMapping("/{id}/devolucao")
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public EmprestimoResponse registrarDevolucao(@PathVariable Long id, @RequestBody(required = false) DevolucaoRequest request) {
        return emprestimoService.registrarDevolucao(id, request, sessaoService.usuarioAtual());
    }
}
