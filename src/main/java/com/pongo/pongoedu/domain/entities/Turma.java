package com.pongo.pongoedu.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "turma")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToMany(mappedBy = "turmas")
    private List<Aluno> alunos;

    @OneToMany(mappedBy = "turma")
    private List<Atividade> atividades;

    @ManyToMany(mappedBy = "turmas")
    private List<Auxiliar> auxiliares;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
}
