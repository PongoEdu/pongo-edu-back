package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.request.RoteiroRequest;
import com.pongo.pongoedu.application.dto.request.SugestaoRoteiroRequest;
import com.pongo.pongoedu.application.dto.response.RoteiroResponse;
import com.pongo.pongoedu.application.dto.response.SugestaoRoteiroResponse;
import com.pongo.pongoedu.application.port.AssistenteRoteiroPort;
import com.pongo.pongoedu.domain.entity.Roteiro;
import com.pongo.pongoedu.domain.entity.RoteiroMaterial;
import com.pongo.pongoedu.domain.entity.Usuario;
import com.pongo.pongoedu.domain.enums.NivelSeguranca;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.AgendamentoRepository;
import com.pongo.pongoedu.domain.repository.ProdutoRepository;
import com.pongo.pongoedu.domain.repository.RoteiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoteiroService {

    private final RoteiroRepository roteiroRepository;
    private final ProdutoRepository produtoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ProdutoService produtoService;
    private final AssistenteRoteiroPort assistenteRoteiro;

    /**
     * RF13 - criacao do roteiro de pratica pelo professor.
     */
    @Transactional
    public RoteiroResponse criar(RoteiroRequest request, Usuario professor) {
        var roteiro = Roteiro.builder()
                .titulo(request.titulo())
                .objetivo(request.objetivo())
                .procedimento(request.procedimento())
                .tema(request.tema())
                .disciplina(request.disciplina())
                .nivelEnsino(request.nivelEnsino())
                .nivelSeguranca(request.nivelSeguranca() != null ? request.nivelSeguranca() : NivelSeguranca.BAIXO)
                .tempoEstimadoMinutos(request.tempoEstimadoMinutos())
                .professor(professor)
                .geradoPorIa(Boolean.TRUE.equals(request.geradoPorIa()))
                .publicado(Boolean.TRUE.equals(request.publicado()))
                .build();

        aplicarMateriais(roteiro, request);
        return RoteiroResponse.de(roteiroRepository.save(roteiro));
    }

    @Transactional(readOnly = true)
    public List<RoteiroResponse> listar(Usuario usuario, Boolean somentePublicados) {
        List<Roteiro> roteiros = Boolean.TRUE.equals(somentePublicados)
                ? roteiroRepository.findByPublicadoTrueOrderByTituloAsc()
                : roteiroRepository.findByProfessorIdOrderByCriadoEmDesc(usuario.getId());
        return roteiros.stream().map(RoteiroResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public RoteiroResponse buscar(Long id) {
        return RoteiroResponse.de(buscarEntidade(id));
    }

    @Transactional
    public RoteiroResponse atualizar(Long id, RoteiroRequest request, Usuario professor) {
        var roteiro = buscarEntidade(id);
        exigirAutoria(roteiro, professor);

        roteiro.setTitulo(request.titulo());
        roteiro.setObjetivo(request.objetivo());
        roteiro.setProcedimento(request.procedimento());
        roteiro.setTema(request.tema());
        roteiro.setDisciplina(request.disciplina());
        roteiro.setNivelEnsino(request.nivelEnsino());
        if (request.nivelSeguranca() != null) {
            roteiro.setNivelSeguranca(request.nivelSeguranca());
        }
        roteiro.setTempoEstimadoMinutos(request.tempoEstimadoMinutos());
        roteiro.setPublicado(Boolean.TRUE.equals(request.publicado()));

        roteiro.getMateriais().clear();
        aplicarMateriais(roteiro, request);
        return RoteiroResponse.de(roteiroRepository.save(roteiro));
    }

    @Transactional
    public void remover(Long id, Usuario professor) {
        var roteiro = buscarEntidade(id);
        exigirAutoria(roteiro, professor);
        if (agendamentoRepository.existsByRoteiroId(id)) {
            throw new DomainException("O roteiro esta vinculado a agendamentos e nao pode ser excluido.");
        }
        roteiroRepository.delete(roteiro);
    }

    /**
     * RF17 - apoio de IA considerando os materiais disponiveis em estoque.
     * A sugestao e apenas devolvida para revisao, nunca gravada automaticamente.
     */
    @Transactional(readOnly = true)
    public SugestaoRoteiroResponse sugerir(SugestaoRoteiroRequest request) {
        var disponiveis = produtoRepository.findByAtivoTrue().stream()
                .filter(produto -> produto.getQuantidadeEstoque() > 0 && !produto.estaVencido())
                .toList();

        if (disponiveis.isEmpty()) {
            throw new DomainException("Nao ha materiais disponiveis em estoque para sugerir um roteiro.");
        }
        return assistenteRoteiro.sugerirRoteiro(request, disponiveis);
    }

    public Roteiro buscarEntidade(Long id) {
        return roteiroRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Roteiro", id));
    }

    private void aplicarMateriais(Roteiro roteiro, RoteiroRequest request) {
        if (request.materiais() == null) {
            roteiro.setPossuiPendenciaMaterial(false);
            return;
        }

        boolean pendencia = false;
        for (var materialRequest : request.materiais()) {
            var produto = produtoService.buscarEntidade(materialRequest.produtoId());
            roteiro.adicionarMaterial(RoteiroMaterial.builder()
                    .produto(produto)
                    .quantidadeNecessaria(materialRequest.quantidadeNecessaria())
                    .observacao(materialRequest.observacao())
                    .build());

            // Excecao 6a/6b do RF13: material faltante nao impede salvar, apenas registra a pendencia.
            if (produto.getQuantidadeEstoque() < materialRequest.quantidadeNecessaria() || produto.estaVencido()) {
                pendencia = true;
            }
        }
        roteiro.setPossuiPendenciaMaterial(pendencia);
    }

    private void exigirAutoria(Roteiro roteiro, Usuario professor) {
        if (!roteiro.getProfessor().getId().equals(professor.getId())) {
            throw new DomainException("O roteiro pertence a outro professor.");
        }
    }
}
