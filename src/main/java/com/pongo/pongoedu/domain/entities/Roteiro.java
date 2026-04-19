package com.pongo.pongoedu.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "roteiros")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roteiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @OneToMany(mappedBy = "roteiro")
    private List<Atividade> atividades;

    @Column
    private LocalDateTime dataCriacao = LocalDateTime.now();
}
