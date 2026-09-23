package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.CategoriaRequest;
import com.pongo.pongoedu.application.dto.response.CategoriaResponse;
import com.pongo.pongoedu.application.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RF04 - Gerenciar Categorias (Quadro 4).
 */
@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public CategoriaResponse criar(@Valid @RequestBody CategoriaRequest request) {
        return categoriaService.criar(request);
    }

    @GetMapping
    public List<CategoriaResponse> listar(@RequestParam(required = false) Boolean somenteAtivas) {
        return categoriaService.listar(somenteAtivas);
    }

    @GetMapping("/{id}")
    public CategoriaResponse buscar(@PathVariable Long id) {
        return categoriaService.buscar(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    public CategoriaResponse atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest request) {
        return categoriaService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AUXILIAR_LABORATORIO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        categoriaService.remover(id);
    }
}
