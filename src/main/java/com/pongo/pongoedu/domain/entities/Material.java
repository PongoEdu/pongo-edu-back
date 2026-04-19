package com.pongo.pongoedu.domain.entities;

import com.pongo.pongoedu.domain.enums.TipoMaterial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "materiais")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column
    private TipoMaterial tipo;

    @Column
    private Integer quantidade = 0;

    @Column
    private Integer quantidadeMinima = 0;

    @Column
    private String localizacao;
}
