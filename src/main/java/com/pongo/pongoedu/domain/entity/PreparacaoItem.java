package com.pongo.pongoedu.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "preparacao_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreparacaoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "preparacao_id", nullable = false)
    private PreparacaoPratica preparacao;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "quantidade_necessaria", nullable = false)
    private Integer quantidadeNecessaria;

    @Column(name = "quantidade_separada", nullable = false)
    @Builder.Default
    private Integer quantidadeSeparada = 0;

    @Column(name = "separado", nullable = false)
    @Builder.Default
    private Boolean separado = false;

    @Column(name = "pendente", nullable = false)
    @Builder.Default
    private Boolean pendente = false;

    @Column(name = "observacao", length = 255)
    private String observacao;
}
