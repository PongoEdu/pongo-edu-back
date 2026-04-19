package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.CadastrarMaterialRequest;
import com.pongo.pongoedu.application.dto.response.MaterialResponse;
import com.pongo.pongoedu.application.usecase.material.CadastrarMaterialUseCase;
import com.pongo.pongoedu.application.usecase.material.ConsultarEstoqueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materiais")
@RequiredArgsConstructor
public class MaterialController {
    private final CadastrarMaterialUseCase cadastrarMaterialUseCase;
    private final ConsultarEstoqueUseCase consultarEstoqueUseCase;

    @PostMapping
    public ResponseEntity<MaterialResponse> cadastrar(@RequestBody CadastrarMaterialRequest request) {
        MaterialResponse response = cadastrarMaterialUseCase.executar(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estoque")
    public ResponseEntity<List<MaterialResponse>> consultarEstoque() {
        List<MaterialResponse> response = consultarEstoqueUseCase.executar();
        return ResponseEntity.ok(response);
    }
}

