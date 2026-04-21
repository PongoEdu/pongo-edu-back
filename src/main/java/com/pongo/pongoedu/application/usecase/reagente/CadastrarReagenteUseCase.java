package com.pongo.pongoedu.application.usecase.reagente;

import com.pongo.pongoedu.application.dto.request.CadastrarReagenteRequest;
import com.pongo.pongoedu.application.dto.response.ReagenteResponse;
import com.pongo.pongoedu.domain.entities.Reagente;
import com.pongo.pongoedu.domain.repository.ReagenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarReagenteUseCase {
    private final ReagenteRepository reagenteRepo;

    public ReagenteResponse executar(CadastrarReagenteRequest request) {
        var reagente = new Reagente();
        reagente.setNome(request.nome());
        reagente.setFormula(request.formula());
        reagente.setDescricao(request.descricao());
        reagente.setQuantidade(request.quantidade());
        reagente.setQuantidadeMinima(request.quantidadeMinima());
        reagente.setUnidade(request.unidade());
        reagente.setLocalizacao(request.localizacao());
        reagente.setTipo(request.tipo());

        reagente = reagenteRepo.salvar(reagente);

        return mapear(reagente);
    }

    public static ReagenteResponse mapear(Reagente r) {
        return new ReagenteResponse(
                r.getId(), r.getNome(), r.getFormula(), r.getDescricao(),
                r.getQuantidade(), r.getQuantidadeMinima(), r.getUnidade(),
                r.getLocalizacao(), r.getTipo(), r.calcularStatus()
        );
    }
}