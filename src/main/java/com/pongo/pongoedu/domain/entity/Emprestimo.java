package com.pongo.pongoedu.domain.entity;

import com.pongo.pongoedu.domain.enums.SituacaoEmprestimo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "professor_id", nullable = false)
    private Usuario professor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_atendimento_id")
    private Usuario auxiliarAtendimento;

    @Column(name = "quantidade", nullable = false)
    @Builder.Default
    private Integer quantidade = 1;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "finalidade", length = 255)
    private String finalidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false, length = 20)
    @Builder.Default
    private SituacaoEmprestimo situacao = SituacaoEmprestimo.PENDENTE;

    @Column(name = "data_solicitacao", nullable = false, updatable = false)
    private LocalDateTime dataSolicitacao;

    @Column(name = "data_retirada")
    private LocalDateTime dataRetirada;

    @Column(name = "data_devolucao")
    private LocalDateTime dataDevolucao;

    @Column(name = "ocorrencia", length = 255)
    private String ocorrencia;

    @Column(name = "observacao", length = 255)
    private String observacao;

    @PrePersist
    void aoCriar() {
        this.dataSolicitacao = LocalDateTime.now();
    }

    public boolean estaEmAberto() {
        return SituacaoEmprestimo.PENDENTE.equals(situacao)
                || SituacaoEmprestimo.APROVADO.equals(situacao)
                || SituacaoEmprestimo.RETIRADO.equals(situacao);
    }

    public boolean estaAtrasado() {
        return SituacaoEmprestimo.RETIRADO.equals(situacao) && dataFim.isBefore(LocalDate.now());
    }
}
