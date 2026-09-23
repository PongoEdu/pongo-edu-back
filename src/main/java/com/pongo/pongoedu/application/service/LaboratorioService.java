package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.request.LaboratorioRequest;
import com.pongo.pongoedu.application.dto.response.LaboratorioResponse;
import com.pongo.pongoedu.domain.entity.Laboratorio;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.LaboratorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LaboratorioService {

    private final LaboratorioRepository laboratorioRepository;

    @Transactional
    public LaboratorioResponse criar(LaboratorioRequest request) {
        var laboratorio = laboratorioRepository.save(Laboratorio.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .capacidade(request.capacidade())
                .ativo(true)
                .build());
        return LaboratorioResponse.de(laboratorio);
    }

    @Transactional(readOnly = true)
    public List<LaboratorioResponse> listar() {
        return laboratorioRepository.findByAtivoTrue().stream().map(LaboratorioResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public LaboratorioResponse buscar(Long id) {
        return LaboratorioResponse.de(buscarEntidade(id));
    }

    @Transactional
    public LaboratorioResponse atualizar(Long id, LaboratorioRequest request) {
        var laboratorio = buscarEntidade(id);
        laboratorio.setNome(request.nome());
        laboratorio.setDescricao(request.descricao());
        laboratorio.setCapacidade(request.capacidade());
        return LaboratorioResponse.de(laboratorioRepository.save(laboratorio));
    }

    @Transactional
    public void inativar(Long id) {
        var laboratorio = buscarEntidade(id);
        laboratorio.setAtivo(false);
        laboratorioRepository.save(laboratorio);
    }

    public Laboratorio buscarEntidade(Long id) {
        return laboratorioRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Laboratorio", id));
    }
}
