package com.pongo.pongoedu.application.usecase.reagente;

import com.pongo.pongoedu.application.dto.response.ReagenteResponse;
import com.pongo.pongoedu.domain.repository.ReagenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarReagentesUseCase {
    private final ReagenteRepository reagenteRepo;

    public List<ReagenteResponse> executar() {
        return reagenteRepo.buscarTodos().stream()
                .map(CadastrarReagenteUseCase::mapear)
                .toList();
    }
}