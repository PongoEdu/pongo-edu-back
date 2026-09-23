package com.pongo.pongoedu.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "laboratorio")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Laboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true, length = 120)
    private String nome;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @Column(name = "capacidade")
    private Integer capacidade;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
