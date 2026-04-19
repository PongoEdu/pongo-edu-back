package com.pongo.pongoedu.infrastructure.persistence.adapter;

import com.pongo.pongoedu.domain.entities.Material;
import com.pongo.pongoedu.domain.repository.MaterialRepository;
import com.pongo.pongoedu.infrastructure.persistence.jpa.MaterialJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MaterialRepositoryImpl implements MaterialRepository {
    private final MaterialJpaRepository materialJpaRepository;

    @Override
    public Material salvar(Material material) {
        return materialJpaRepository.save(material);
    }

    @Override
    public Optional<Material> buscarPorId(Long id) {
        return materialJpaRepository.findById(id);
    }

    @Override
    public List<Material> buscarTodos() {
        return materialJpaRepository.findAll();
    }

    @Override
    public void deletar(Long id) {
        materialJpaRepository.deleteById(id);
    }
}

