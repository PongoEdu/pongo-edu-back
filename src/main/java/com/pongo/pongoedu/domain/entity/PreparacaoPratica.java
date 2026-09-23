package com.pongo.pongoedu.domain.entity;

import com.pongo.pongoedu.domain.enums.SituacaoPreparacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "preparacao_pratica")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreparacaoPratica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "agendamento_id", nullable = false, unique = true)
    private Agendamento agendamento;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "auxiliar_id", nullable = false)
    private Usuario auxiliar;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false, length = 20)
    @Builder.Default
    private SituacaoPreparacao situacao = SituacaoPreparacao.PENDENTE;

    @Column(name = "data_inicio", nullable = false, updatable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(name = "observacao", length = 255)
    private String observacao;

    @OneToMany(mappedBy = "preparacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PreparacaoItem> itens = new ArrayList<>();

    @PrePersist
    void aoCriar() {
        this.dataInicio = LocalDateTime.now();
    }

    public void adicionarItem(PreparacaoItem item) {
        item.setPreparacao(this);
        this.itens.add(item);
    }

    public boolean possuiPendencia() {
        return itens.stream().anyMatch(item -> Boolean.TRUE.equals(item.getPendente()));
    }
}
