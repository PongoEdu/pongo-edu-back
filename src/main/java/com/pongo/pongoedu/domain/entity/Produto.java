package com.pongo.pongoedu.domain.entity;

import com.pongo.pongoedu.domain.enums.StatusEstoque;
import com.pongo.pongoedu.domain.enums.UnidadeMedida;
import com.pongo.pongoedu.domain.exception.DomainException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "produto")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 60)
    private String codigo;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidade_medida", nullable = false, length = 20)
    private UnidadeMedida unidadeMedida;

    @Column(name = "quantidade_estoque", nullable = false)
    @Builder.Default
    private Integer quantidadeEstoque = 0;

    @Column(name = "estoque_minimo", nullable = false)
    @Builder.Default
    private Integer estoqueMinimo = 0;

    @Column(name = "localizacao", length = 120)
    private String localizacao;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    void aoCriar() {
        this.criadoEm = LocalDateTime.now();
    }

    @PreUpdate
    void aoAtualizar() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public StatusEstoque getStatusEstoque() {
        if (quantidadeEstoque <= 0) return StatusEstoque.ESGOTADO;
        if (quantidadeEstoque <= estoqueMinimo / 2) return StatusEstoque.CRITICO;
        if (quantidadeEstoque <= estoqueMinimo) return StatusEstoque.BAIXO;
        return StatusEstoque.DISPONIVEL;
    }

    public boolean atingiuEstoqueMinimo() {
        return quantidadeEstoque <= estoqueMinimo;
    }

    public boolean estaVencido() {
        return lote != null && lote.estaVencido();
    }

    public boolean permiteEmprestimo() {
        return categoria != null && Boolean.TRUE.equals(categoria.getPermiteEmprestimo());
    }

    public void adicionarEstoque(int quantidade) {
        if (quantidade <= 0) throw new DomainException("A quantidade de entrada deve ser positiva");
        this.quantidadeEstoque += quantidade;
    }

    public void retirarEstoque(int quantidade) {
        if (quantidade <= 0) throw new DomainException("A quantidade de saída deve ser positiva");
        if (quantidade > this.quantidadeEstoque) {
            throw new DomainException("Estoque insuficiente para o produto " + nome
                    + ": disponível " + quantidadeEstoque + ", solicitado " + quantidade);
        }
        this.quantidadeEstoque -= quantidade;
    }
}
