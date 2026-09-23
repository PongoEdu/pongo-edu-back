package com.pongo.pongoedu.domain.entity;

import com.pongo.pongoedu.domain.enums.SituacaoAgendamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "agendamento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "professor_id", nullable = false)
    private Usuario professor;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "laboratorio_id", nullable = false)
    private Laboratorio laboratorio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roteiro_id")
    private Roteiro roteiro;

    @Column(name = "turma", length = 60)
    private String turma;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false, length = 20)
    @Builder.Default
    private SituacaoAgendamento situacao = SituacaoAgendamento.AGENDADO;

    @Column(name = "observacao", length = 255)
    private String observacao;

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

    public boolean podeSerAlteradoPeloProfessor() {
        return SituacaoAgendamento.AGENDADO.equals(situacao);
    }
}
