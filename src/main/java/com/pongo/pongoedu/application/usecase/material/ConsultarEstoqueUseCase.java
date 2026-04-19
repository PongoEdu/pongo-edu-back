package com.pongo.pongoedu.application.usecase.material;

import com.pongo.pongoedu.application.dto.response.MaterialResponse;
import com.pongo.pongoedu.domain.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultarEstoqueUseCase {
    private final MaterialRepository materialRepository;

    public List<MaterialResponse> executar() {
        return materialRepository.buscarTodos()
            .stream()
            .map(this::mapearParaResponse)
            .collect(Collectors.toList());
    }

    private MaterialResponse mapearParaResponse(com.pongo.pongoedu.domain.entities.Material material) {
        return new MaterialResponse(
            material.getId(),
            material.getNome(),
            material.getDescricao(),
            material.getTipo(),
            material.getQuantidade(),
            material.getLocalizacao()
        );
    }
}
