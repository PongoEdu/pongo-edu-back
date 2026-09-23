package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.request.DevolucaoRequest;
import com.pongo.pongoedu.application.dto.request.EmprestimoRequest;
import com.pongo.pongoedu.application.dto.response.EmprestimoResponse;
import com.pongo.pongoedu.domain.entity.Emprestimo;
import com.pongo.pongoedu.domain.entity.Usuario;
import com.pongo.pongoedu.domain.enums.OrigemMovimentacao;
import com.pongo.pongoedu.domain.enums.Perfil;
import com.pongo.pongoedu.domain.enums.SituacaoEmprestimo;
import com.pongo.pongoedu.domain.enums.TipoNotificacao;
import com.pongo.pongoedu.domain.exception.ConflitoException;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.EmprestimoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private static final List<SituacaoEmprestimo> SITUACOES_EM_ABERTO =
            List.of(SituacaoEmprestimo.PENDENTE, SituacaoEmprestimo.APROVADO, SituacaoEmprestimo.RETIRADO);

    private final EmprestimoRepository emprestimoRepository;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;
    private final NotificacaoService notificacaoService;

    /**
     * RF11 - o professor solicita o emprestimo de um equipamento.
     */
    @Transactional
    public EmprestimoResponse solicitar(EmprestimoRequest request, Usuario professor) {
        var produto = produtoService.buscarEntidade(request.produtoId());

        // Excecao 2a/2b do RF11.
        if (!produto.permiteEmprestimo()) {
            throw new DomainException("A categoria " + produto.getCategoria().getNome()
                    + " nao permite emprestimo de equipamentos.");
        }
        if (request.dataFim().isBefore(request.dataInicio())) {
            throw new DomainException("A data final do emprestimo nao pode ser anterior a data inicial.");
        }
        if (produto.getQuantidadeEstoque() < request.quantidade()) {
            throw new DomainException("Quantidade indisponivel para emprestimo: disponivel "
                    + produto.getQuantidadeEstoque() + ".");
        }

        // Excecao 3a/3b do RF11: equipamento ja emprestado no periodo pretendido.
        var conflitos = emprestimoRepository.buscarConflitos(
                produto.getId(), request.dataInicio(), request.dataFim(), SITUACOES_EM_ABERTO);
        int reservado = conflitos.stream().mapToInt(Emprestimo::getQuantidade).sum();
        if (reservado + request.quantidade() > produto.getQuantidadeEstoque()) {
            throw new ConflitoException("O equipamento ja esta reservado no periodo informado. "
                    + "Reservas no periodo: " + reservado + " de " + produto.getQuantidadeEstoque() + ".");
        }

        var emprestimo = emprestimoRepository.save(Emprestimo.builder()
                .produto(produto)
                .professor(professor)
                .quantidade(request.quantidade())
                .dataInicio(request.dataInicio())
                .dataFim(request.dataFim())
                .finalidade(request.finalidade())
                .situacao(SituacaoEmprestimo.PENDENTE)
                .build());

        notificacaoService.notificarPerfil(Perfil.AUXILIAR_LABORATORIO, TipoNotificacao.SOLICITACAO_EMPRESTIMO,
                "PongoEdu - nova solicitacao de emprestimo",
                professor.getNome() + " solicitou " + request.quantidade() + "x " + produto.getNome()
                        + " de " + request.dataInicio() + " a " + request.dataFim() + ".");

        return EmprestimoResponse.de(emprestimo);
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> listar(Usuario usuario, SituacaoEmprestimo situacao) {
        List<Emprestimo> emprestimos;
        if (usuario.isProfessor()) {
            emprestimos = emprestimoRepository.findByProfessorIdOrderByDataSolicitacaoDesc(usuario.getId());
        } else if (situacao != null) {
            emprestimos = emprestimoRepository.findBySituacaoOrderByDataSolicitacaoDesc(situacao);
        } else {
            emprestimos = emprestimoRepository.findAllByOrderByDataSolicitacaoDesc();
        }
        return emprestimos.stream()
                .filter(emprestimo -> situacao == null || situacao.equals(emprestimo.getSituacao()))
                .map(EmprestimoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmprestimoResponse buscar(Long id) {
        return EmprestimoResponse.de(buscarEntidade(id));
    }

    /**
     * RF12 - o auxiliar aprova a solicitacao.
     */
    @Transactional
    public EmprestimoResponse aprovar(Long id, String observacao, Usuario auxiliar) {
        var emprestimo = buscarEntidade(id);
        exigirSituacao(emprestimo, SituacaoEmprestimo.PENDENTE);

        if (emprestimo.getProduto().getQuantidadeEstoque() < emprestimo.getQuantidade()) {
            throw new DomainException("O equipamento esta indisponivel na data solicitada. "
                    + "A solicitacao permanece pendente para renegociacao do periodo.");
        }

        emprestimo.setSituacao(SituacaoEmprestimo.APROVADO);
        emprestimo.setAuxiliarAtendimento(auxiliar);
        emprestimo.setObservacao(observacao);
        emprestimoRepository.save(emprestimo);

        notificacaoService.registrar(emprestimo.getProfessor(), TipoNotificacao.SOLICITACAO_EMPRESTIMO,
                "PongoEdu - emprestimo aprovado",
                "O emprestimo de " + emprestimo.getProduto().getNome() + " foi aprovado.");

        return EmprestimoResponse.de(emprestimo);
    }

    @Transactional
    public EmprestimoResponse recusar(Long id, String observacao, Usuario auxiliar) {
        var emprestimo = buscarEntidade(id);
        exigirSituacao(emprestimo, SituacaoEmprestimo.PENDENTE);

        emprestimo.setSituacao(SituacaoEmprestimo.RECUSADO);
        emprestimo.setAuxiliarAtendimento(auxiliar);
        emprestimo.setObservacao(observacao);
        emprestimoRepository.save(emprestimo);

        notificacaoService.registrar(emprestimo.getProfessor(), TipoNotificacao.SOLICITACAO_EMPRESTIMO,
                "PongoEdu - emprestimo recusado",
                "O emprestimo de " + emprestimo.getProduto().getNome() + " foi recusado. " + observacao);

        return EmprestimoResponse.de(emprestimo);
    }

    /**
     * RF12 - retirada do equipamento: registra a saida de estoque.
     */
    @Transactional
    public EmprestimoResponse registrarRetirada(Long id, Usuario auxiliar) {
        var emprestimo = buscarEntidade(id);
        exigirSituacao(emprestimo, SituacaoEmprestimo.APROVADO);

        estoqueService.registrarSaida(emprestimo.getProduto(), emprestimo.getQuantidade(),
                OrigemMovimentacao.EMPRESTIMO, auxiliar, emprestimo.getId(),
                "Emprestimo " + emprestimo.getId() + " - " + emprestimo.getProfessor().getNome());

        emprestimo.setSituacao(SituacaoEmprestimo.RETIRADO);
        emprestimo.setDataRetirada(LocalDateTime.now());
        emprestimo.setAuxiliarAtendimento(auxiliar);
        return EmprestimoResponse.de(emprestimoRepository.save(emprestimo));
    }

    /**
     * RF12 - devolucao: registra a entrada correspondente e encerra o emprestimo.
     * Excecao 5a/5b: havendo avaria, a ocorrencia e registrada e o emprestimo segue em aberto.
     */
    @Transactional
    public EmprestimoResponse registrarDevolucao(Long id, DevolucaoRequest request, Usuario auxiliar) {
        var emprestimo = buscarEntidade(id);
        exigirSituacao(emprestimo, SituacaoEmprestimo.RETIRADO);

        boolean comOcorrencia = request != null && request.ocorrencia() != null && !request.ocorrencia().isBlank();
        if (comOcorrencia) {
            emprestimo.setOcorrencia(request.ocorrencia());
            return EmprestimoResponse.de(emprestimoRepository.save(emprestimo));
        }

        estoqueService.registrarEntrada(emprestimo.getProduto(), emprestimo.getQuantidade(),
                OrigemMovimentacao.DEVOLUCAO_EMPRESTIMO, auxiliar, emprestimo.getId(),
                "Devolucao do emprestimo " + emprestimo.getId());

        emprestimo.setSituacao(SituacaoEmprestimo.DEVOLVIDO);
        emprestimo.setDataDevolucao(LocalDateTime.now());
        emprestimo.setAuxiliarAtendimento(auxiliar);
        return EmprestimoResponse.de(emprestimoRepository.save(emprestimo));
    }

    private Emprestimo buscarEntidade(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Emprestimo", id));
    }

    private void exigirSituacao(Emprestimo emprestimo, SituacaoEmprestimo esperada) {
        if (!esperada.equals(emprestimo.getSituacao())) {
            throw new DomainException("Operacao invalida: o emprestimo esta na situacao "
                    + emprestimo.getSituacao() + " e era esperada a situacao " + esperada + ".");
        }
    }
}
