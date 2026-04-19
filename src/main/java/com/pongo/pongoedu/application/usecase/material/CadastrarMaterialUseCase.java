package com.pongo.pongoedu.application.usecase.material;

import com.pongo.pongoedu.application.dto.request.CadastrarMaterialRequest;
import com.pongo.pongoedu.application.dto.response.MaterialResponse;
import com.pongo.pongoedu.domain.entities.Material;
import com.pongo.pongoedu.domain.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarMaterialUseCase {
    private final MaterialRepository materialRepository;

    public MaterialResponse executar(CadastrarMaterialRequest request) {
        Material material = new Material();
        material.setNome(request.getNome());
        material.setDescricao(request.getDescricao());
        material.setTipo(request.getTipo());
        material.setQuantidade(request.getQuantidade());
        material.setLocalizacao(request.getLocalizacao());

        material = materialRepository.salvar(material);

        return mapearParaResponse(material);
    }

    private MaterialResponse mapearParaResponse(Material material) {
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
