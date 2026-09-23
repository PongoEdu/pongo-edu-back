package com.pongo.pongoedu.domain.entity;

import com.pongo.pongoedu.domain.enums.SituacaoSolicitacaoCompra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacao_compra")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "quantidade_sugerida", nullable = false)
    private Integer quantidadeSugerida;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false, length = 20)
    @Builder.Default
    private SituacaoSolicitacaoCompra situacao = SituacaoSolicitacaoCompra.PENDENTE;

    @Column(name = "gerada_automaticamente", nullable = false)
    @Builder.Default
    private Boolean geradaAutomaticamente = true;

    @Column(name = "data_geracao", nullable = false, updatable = false)
    private LocalDateTime dataGeracao;

    @Column(name = "data_atendimento")
    private LocalDateTime dataAtendimento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_atendimento_id")
    private Usuario usuarioAtendimento;

    @Column(name = "observacao", length = 255)
    private String observacao;

    @PrePersist
    void aoCriar() {
        this.dataGeracao = LocalDateTime.now();
    }

    public boolean estaPendente() {
        return SituacaoSolicitacaoCompra.PENDENTE.equals(situacao);
    }
}
