package com.pongo.pongoedu.domain.entities;

import com.pongo.pongoedu.domain.enums.EfeitoVisual;
import com.pongo.pongoedu.domain.enums.NivelSeguranca;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "experimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Experimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String passoAPasso;

    @Column
    private Integer tempoEstimado;

    @Column
    private String serie;

    @Column
    private String disciplina;

    @Enumerated(EnumType.STRING)
    @Column
    private NivelSeguranca nivelSeguranca;

    @Enumerated(EnumType.STRING)
    @Column
    private EfeitoVisual efeito;

    @ManyToMany
    @JoinTable(
            name = "experimento_reagente",
            joinColumns = @JoinColumn(name = "experimento_id"),
            inverseJoinColumns = @JoinColumn(name = "reagente_id")
    )
    private List<Reagente> reagentes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Experimento)) return false;
        return id != null && id.equals(((Experimento) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}