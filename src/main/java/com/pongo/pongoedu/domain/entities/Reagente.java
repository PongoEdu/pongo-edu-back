package com.pongo.pongoedu.domain.entities;

import com.pongo.pongoedu.domain.enums.StatusEstoque;
import com.pongo.pongoedu.domain.enums.TipoMaterial;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "reagente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reagente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String formula;

    @Column
    private String descricao;

    @Column(nullable = false)
    private Integer quantidade = 0;

    @Column(nullable = false)
    private Integer quantidadeMinima = 0;

    @Column
    private String unidade;

    @Column
    private String localizacao;

    @Enumerated(EnumType.STRING)
    @Column
    private TipoMaterial tipo;

    public StatusEstoque calcularStatus() {
        if (quantidade <= 0) return StatusEstoque.ESGOTADO;
        if (quantidade <= quantidadeMinima / 2) return StatusEstoque.CRITICO;
        if (quantidade <= quantidadeMinima) return StatusEstoque.BAIXO;
        return StatusEstoque.DISPONIVEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reagente)) return false;
        return id != null && id.equals(((Reagente) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}