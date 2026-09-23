package com.pongo.pongoedu.domain.entity;

import com.pongo.pongoedu.domain.enums.TipoEntrada;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recebimento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recebimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_entrada", nullable = false, length = 30)
    private TipoEntrada tipoEntrada;

    @Column(name = "numero_nota_fiscal", length = 60)
    private String numeroNotaFiscal;

    @Column(name = "fornecedor", length = 150)
    private String fornecedor;

    @Column(name = "data_recebimento", nullable = false)
    private LocalDate dataRecebimento;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario auxiliar;

    @Column(name = "observacao", length = 255)
    private String observacao;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "recebimento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecebimentoItem> itens = new ArrayList<>();

    @PrePersist
    void aoCriar() {
        this.criadoEm = LocalDateTime.now();
    }

    public void adicionarItem(RecebimentoItem item) {
        item.setRecebimento(this);
        this.itens.add(item);
    }
}
