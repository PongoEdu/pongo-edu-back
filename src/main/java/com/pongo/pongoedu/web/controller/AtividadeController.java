package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.response.AtividadeResponse;
import com.pongo.pongoedu.application.dto.response.RankingResponse;
import com.pongo.pongoedu.application.usecase.gamificacao.CompletarAtividadeUseCase;
import com.pongo.pongoedu.application.usecase.gamificacao.ConsultarRankingUseCase;
import com.pongo.pongoedu.application.dto.response.AlunoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
public class AtividadeController {
    private final CompletarAtividadeUseCase completarAtividadeUseCase;
    private final ConsultarRankingUseCase consultarRankingUseCase;

    @PutMapping("/{atividadeId}/completar")
    public ResponseEntity<AtividadeResponse> completar(@PathVariable Long atividadeId) {
        AtividadeResponse response = completarAtividadeUseCase.executar(atividadeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/turmas/{turmaId}/ranking")
    public ResponseEntity<List<RankingResponse>> consultarRanking(@PathVariable Long turmaId) {
        List<RankingResponse> response = consultarRankingUseCase.executar(turmaId);
        return ResponseEntity.ok(response);
    }
}

