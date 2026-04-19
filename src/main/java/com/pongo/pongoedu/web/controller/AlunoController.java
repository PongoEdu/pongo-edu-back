package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.response.AlunoResponse;
import com.pongo.pongoedu.application.dto.response.DashboardAlunoResponse;
import com.pongo.pongoedu.application.usecase.aluno.ConsultarDashboardAlunoUseCase;
import com.pongo.pongoedu.application.usecase.aluno.MatricularAlunoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {
    private final MatricularAlunoUseCase matricularAlunoUseCase;
    private final ConsultarDashboardAlunoUseCase consultarDashboardAlunoUseCase;

    @PostMapping("/{alunoId}/turmas/{turmaId}")
    public ResponseEntity<AlunoResponse> matricular(@PathVariable Long alunoId, @PathVariable Long turmaId) {
        AlunoResponse response = matricularAlunoUseCase.executar(alunoId, turmaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{alunoId}/dashboard")
    public ResponseEntity<DashboardAlunoResponse> consultarDashboard(@PathVariable Long alunoId) {
        DashboardAlunoResponse response = consultarDashboardAlunoUseCase.executar(alunoId);
        return ResponseEntity.ok(response);
    }
}

