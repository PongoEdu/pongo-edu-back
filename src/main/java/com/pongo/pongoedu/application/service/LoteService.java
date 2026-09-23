package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.request.LoteRequest;
import com.pongo.pongoedu.application.dto.response.LoteResponse;
import com.pongo.pongoedu.domain.entity.Lote;
import com.pongo.pongoedu.domain.exception.ConflitoException;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.LoteRepository;
import com.pongo.pongoedu.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public LoteResponse criar(LoteRequest request) {
        if (loteRepository.existsByCodigoIgnoreCase(request.codigo())) {
            throw new ConflitoException("Ja existe um lote com o codigo " + request.codigo());
        }
        validarDatas(request);
        var lote = loteRepository.save(Lote.builder()
                .codigo(request.codigo())
                .fornecedor(request.fornecedor())
                .dataFabricacao(request.dataFabricacao())
                .dataValidade(request.dataValidade())
                .observacao(request.observacao())
                .build());
        return LoteResponse.de(lote);
    }

    @Transactional(readOnly = true)
    public List<LoteResponse> listar() {
        return loteRepository.findAll().stream().map(LoteResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public LoteResponse buscar(Long id) {
        return LoteResponse.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<LoteResponse> listarVencidos() {
        return loteRepository.findByDataValidadeBefore(LocalDate.now()).stream()
                .map(LoteResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LoteResponse> listarAVencer(int dias) {
        return loteRepository.findByDataValidadeBetween(LocalDate.now(), LocalDate.now().plusDays(dias)).stream()
                .map(LoteResponse::de)
                .toList();
    }

    @Transactional
    public LoteResponse atualizar(Long id, LoteRequest request) {
        var lote = buscarEntidade(id);
        boolean codigoMudou = !lote.getCodigo().equalsIgnoreCase(request.codigo());
        if (codigoMudou && loteRepository.existsByCodigoIgnoreCase(request.codigo())) {
            throw new ConflitoException("Ja existe um lote com o codigo " + request.codigo());
        }
        validarDatas(request);
        lote.setCodigo(request.codigo());
        lote.setFornecedor(request.fornecedor());
        lote.setDataFabricacao(request.dataFabricacao());
        lote.setDataValidade(request.dataValidade());
        lote.setObservacao(request.observacao());
        return LoteResponse.de(loteRepository.save(lote));
    }

    @Transactional
    public void remover(Long id) {
        var lote = buscarEntidade(id);
        if (produtoRepository.existsByLoteId(id)) {
            throw new DomainException("O lote possui produtos vinculados e nao pode ser excluido.");
        }
        loteRepository.delete(lote);
    }

    public Lote buscarEntidade(Long id) {
        return loteRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Lote", id));
    }

    private void validarDatas(LoteRequest request) {
        if (request.dataFabricacao() != null && request.dataValidade() != null
                && request.dataValidade().isBefore(request.dataFabricacao())) {
            throw new DomainException("A data de validade nao pode ser anterior a data de fabricacao.");
        }
    }
}
