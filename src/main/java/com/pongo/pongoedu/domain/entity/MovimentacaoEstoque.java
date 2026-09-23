package com.pongo.pongoedu.domain.entity;

import com.pongo.pongoedu.domain.enums.OrigemMovimentacao;
import com.pongo.pongoedu.domain.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_estoque")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoMovimentacao tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem", nullable = false, length = 40)
    private OrigemMovimentacao origem;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "quantidade_anterior", nullable = false)
    private Integer quantidadeAnterior;

    @Column(name = "quantidade_resultante", nullable = false)
    private Integer quantidadeResultante;

    @Column(name = "referencia_id")
    private Long referenciaId;

    @Column(name = "observacao", length = 255)
    private String observacao;

    @Column(name = "data_movimentacao", nullable = false, updatable = false)
    private LocalDateTime dataMovimentacao;

    @PrePersist
    void aoCriar() {
        this.dataMovimentacao = LocalDateTime.now();
    }
}
