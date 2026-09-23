package com.pongo.pongoedu.domain.entity;

import com.pongo.pongoedu.domain.enums.NivelEnsino;
import com.pongo.pongoedu.domain.enums.NivelSeguranca;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roteiro")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Roteiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "objetivo", columnDefinition = "TEXT")
    private String objetivo;

    @Column(name = "procedimento", columnDefinition = "TEXT")
    private String procedimento;

    @Column(name = "tema", length = 150)
    private String tema;

    @Column(name = "disciplina", length = 80)
    private String disciplina;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_ensino", length = 30)
    private NivelEnsino nivelEnsino;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_seguranca", length = 20)
    @Builder.Default
    private NivelSeguranca nivelSeguranca = NivelSeguranca.BAIXO;

    @Column(name = "tempo_estimado_minutos")
    private Integer tempoEstimadoMinutos;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "professor_id", nullable = false)
    private Usuario professor;

    @Column(name = "gerado_por_ia", nullable = false)
    @Builder.Default
    private Boolean geradoPorIa = false;

    @Column(name = "publicado", nullable = false)
    @Builder.Default
    private Boolean publicado = false;

    @Column(name = "possui_pendencia_material", nullable = false)
    @Builder.Default
    private Boolean possuiPendenciaMaterial = false;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "roteiro", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RoteiroMaterial> materiais = new ArrayList<>();

    @PrePersist
    void aoCriar() {
        this.criadoEm = LocalDateTime.now();
    }

    @PreUpdate
    void aoAtualizar() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public void adicionarMaterial(RoteiroMaterial material) {
        material.setRoteiro(this);
        this.materiais.add(material);
    }
}
