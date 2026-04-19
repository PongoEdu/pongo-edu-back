package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.domain.entities.Roteiro;
import com.pongo.pongoedu.domain.repository.RoteiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roteiros")
@RequiredArgsConstructor
public class RoteiroController {
    private final RoteiroRepository roteiroRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Roteiro> buscarPorId(@PathVariable Long id) {
        Optional<Roteiro> roteiro = roteiroRepository.buscarPorId(id);
        return roteiro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Roteiro>> buscarTodos() {
        return ResponseEntity.ok(roteiroRepository.buscarTodos());
    }
}

