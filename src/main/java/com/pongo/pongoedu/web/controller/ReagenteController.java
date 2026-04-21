package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.CadastrarReagenteRequest;
import com.pongo.pongoedu.application.dto.response.ReagenteResponse;
import com.pongo.pongoedu.application.usecase.reagente.CadastrarReagenteUseCase;
import com.pongo.pongoedu.application.usecase.reagente.ListarReagentesUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reagentes")
@RequiredArgsConstructor
public class ReagenteController {

    private final CadastrarReagenteUseCase cadastrarReagente;
    private final ListarReagentesUseCase listarReagentes;

    @GetMapping
    @PreAuthorize("hasAnyRole('AUXILIAR', 'PROFESSOR')")
    public ResponseEntity<List<ReagenteResponse>> listar() {
        return ResponseEntity.ok(listarReagentes.executar());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('AUXILIAR')")
    public ResponseEntity<ReagenteResponse> cadastrar(
            @RequestBody @Valid CadastrarReagenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cadastrarReagente.executar(request));
    }
}