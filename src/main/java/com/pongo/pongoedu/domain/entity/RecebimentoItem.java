package com.pongo.pongoedu.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "recebimento_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecebimentoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recebimento_id", nullable = false)
    private Recebimento recebimento;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "valor_unitario", precision = 12, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "vencido_confirmado", nullable = false)
    @Builder.Default
    private Boolean vencidoConfirmado = false;
}
