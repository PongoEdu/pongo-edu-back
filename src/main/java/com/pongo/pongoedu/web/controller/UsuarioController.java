package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.UsuarioAtualizacaoRequest;
import com.pongo.pongoedu.application.dto.request.UsuarioRequest;
import com.pongo.pongoedu.application.dto.response.UsuarioResponse;
import com.pongo.pongoedu.application.service.UsuarioService;
import com.pongo.pongoedu.domain.enums.Perfil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RF19 - Gerenciar Usuarios (Quadro 3).
 */
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse criar(@Valid @RequestBody UsuarioRequest request) {
        return usuarioService.criar(request);
    }

    @GetMapping
    public List<UsuarioResponse> listar(@RequestParam(required = false) Perfil perfil) {
        return usuarioService.listar(perfil);
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable Long id) {
        return usuarioService.buscar(id);
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioAtualizacaoRequest request) {
        return usuarioService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioResponse> remover(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.remover(id));
    }
}
