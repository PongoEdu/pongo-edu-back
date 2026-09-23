package com.pongo.pongoedu.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "roteiro_material",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_roteiro_material",
                columnNames = {"roteiro_id", "produto_id"}
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoteiroMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roteiro_id", nullable = false)
    private Roteiro roteiro;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "quantidade_necessaria", nullable = false)
    @Builder.Default
    private Integer quantidadeNecessaria = 1;

    @Column(name = "observacao", length = 255)
    private String observacao;
}
