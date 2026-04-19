package com.pongo.pongoedu.domain.entities;

import com.pongo.pongoedu.domain.enums.StatusAtividade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "atividade")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column
    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @ManyToOne
    @JoinColumn(name = "roteiro_id")
    private Roteiro roteiro;

    @Column(nullable = false)
    private Integer xpRecompensa = 0;

    @Enumerated(EnumType.STRING)
    @Column
    private StatusAtividade status;

    @Column
    private LocalDateTime dataVencimento;

    @Column
    private LocalDate dataEntrega;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
}
