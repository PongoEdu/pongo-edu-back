package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.response.ExperimentoDisponibilidadeResponse;
import com.pongo.pongoedu.application.usecase.experimento.ListarExperimentosComDisponibilidadeUseCase;
import com.pongo.pongoedu.domain.enums.EfeitoVisual;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experimentos")
@RequiredArgsConstructor
public class ExperimentoController {

    private final ListarExperimentosComDisponibilidadeUseCase listarExperimentos;

    @GetMapping
    public ResponseEntity<List<ExperimentoDisponibilidadeResponse>> listar(
            @RequestParam(required = false) EfeitoVisual efeito) {
        return ResponseEntity.ok(listarExperimentos.executar(efeito));
    }
}