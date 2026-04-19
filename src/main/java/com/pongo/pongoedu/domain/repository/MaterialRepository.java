package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entities.Material;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository {
    Material salvar(Material material);
    Optional<Material> buscarPorId(Long id);
    List<Material> buscarTodos();
    void deletar(Long id);
}
