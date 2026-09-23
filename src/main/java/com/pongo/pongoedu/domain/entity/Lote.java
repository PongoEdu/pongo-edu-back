package com.pongo.pongoedu.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lote")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 60)
    private String codigo;

    @Column(name = "fornecedor", length = 150)
    private String fornecedor;

    @Column(name = "data_fabricacao")
    private LocalDate dataFabricacao;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(name = "observacao", length = 255)
    private String observacao;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void aoCriar() {
        this.criadoEm = LocalDateTime.now();
    }

    public boolean estaVencido() {
        return dataValidade != null && dataValidade.isBefore(LocalDate.now());
    }

    public boolean venceEmAte(int dias) {
        return dataValidade != null && !estaVencido()
                && !dataValidade.isAfter(LocalDate.now().plusDays(dias));
    }
}
