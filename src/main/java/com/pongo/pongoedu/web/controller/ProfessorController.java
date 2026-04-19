package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.CriarAtividadeRequest;
import com.pongo.pongoedu.application.dto.response.AtividadeResponse;
import com.pongo.pongoedu.application.dto.response.DashboardProfessorResponse;
import com.pongo.pongoedu.application.usecase.professor.ConsultarDashboardProfessorUseCase;
import com.pongo.pongoedu.application.usecase.professor.CriarAtividadeUseCase;
import com.pongo.pongoedu.application.usecase.professor.CriarRoteiroUseCase;
import com.pongo.pongoedu.domain.entities.Roteiro;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/professores")
@RequiredArgsConstructor
public class ProfessorController {
    private final CriarAtividadeUseCase criarAtividadeUseCase;
    private final CriarRoteiroUseCase criarRoteiroUseCase;
    private final ConsultarDashboardProfessorUseCase consultarDashboardProfessorUseCase;

    @PostMapping("/{professorId}/atividades")
    public ResponseEntity<AtividadeResponse> criarAtividade(
            @PathVariable Long professorId,
            @RequestBody CriarAtividadeRequest request) {
        AtividadeResponse response = criarAtividadeUseCase.executar(professorId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{professorId}/roteiros")
    public ResponseEntity<Roteiro> criarRoteiro(
            @PathVariable Long professorId,
            @RequestParam String titulo,
            @RequestParam String descricao) {
        Roteiro response = criarRoteiroUseCase.executar(professorId, titulo, descricao);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{professorId}/dashboard")
    public ResponseEntity<DashboardProfessorResponse> consultarDashboard(@PathVariable Long professorId) {
        DashboardProfessorResponse response = consultarDashboardProfessorUseCase.executar(professorId);
        return ResponseEntity.ok(response);
    }
}

