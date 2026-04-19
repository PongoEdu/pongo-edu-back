package com.pongo.pongoedu.domain.entities;

import com.pongo.pongoedu.domain.exception.DomainException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "aluno")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(unique = true)
    private String matricula;

    @Column(nullable = false)
    private Integer xpTotal = 0;

    @Column(nullable = false)
    private Integer ofensivaDias = 0;

    @Column
    private LocalDate ultimaAtividade;

    @ManyToMany
    @JoinTable(
            name = "aluno_turma",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "turma_id")
    )
    private List<Turma> turmas;

    @ManyToMany
    @JoinTable(
            name = "aluno_atividade",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "atividade_id")
    )
    private List<Atividade> atividadesCompletadas;

    public void matricularEmTurma(Turma turma) {
        if (this.turmas.contains(turma))
            throw new DomainException("Aluno já matriculado nesta turma");
        this.turmas.add(turma);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aluno)) return false;
        return id != null && id.equals(((Aluno) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}