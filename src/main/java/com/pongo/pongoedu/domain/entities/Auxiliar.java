package com.pongo.pongoedu.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "auxiliar")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auxiliar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column
    private String setor;

    @ManyToMany
    @JoinTable(
        name = "auxiliar_turma",
        joinColumns = @JoinColumn(name = "auxiliar_id"),
        inverseJoinColumns = @JoinColumn(name = "turma_id")
    )
    private List<Turma> turmas;
}
