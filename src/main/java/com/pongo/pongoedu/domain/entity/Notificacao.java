package com.pongo.pongoedu.domain.entity;

import com.pongo.pongoedu.domain.enums.SituacaoNotificacao;
import com.pongo.pongoedu.domain.enums.TipoNotificacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario destinatario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 40)
    private TipoNotificacao tipo;

    @Column(name = "assunto", nullable = false, length = 150)
    private String assunto;

    @Column(name = "mensagem", columnDefinition = "TEXT")
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false, length = 20)
    @Builder.Default
    private SituacaoNotificacao situacao = SituacaoNotificacao.PENDENTE;

    @Column(name = "tentativas", nullable = false)
    @Builder.Default
    private Integer tentativas = 0;

    @Column(name = "erro", length = 255)
    private String erro;

    @Column(name = "data_geracao", nullable = false, updatable = false)
    private LocalDateTime dataGeracao;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @PrePersist
    void aoCriar() {
        this.dataGeracao = LocalDateTime.now();
    }
}
