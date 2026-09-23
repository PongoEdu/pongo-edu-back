package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.response.NotificacaoResponse;
import com.pongo.pongoedu.application.port.NotificadorPort;
import com.pongo.pongoedu.domain.entity.Notificacao;
import com.pongo.pongoedu.domain.entity.Usuario;
import com.pongo.pongoedu.domain.enums.Perfil;
import com.pongo.pongoedu.domain.enums.SituacaoAgendamento;
import com.pongo.pongoedu.domain.enums.SituacaoNotificacao;
import com.pongo.pongoedu.domain.enums.TipoMovimentacao;
import com.pongo.pongoedu.domain.enums.TipoNotificacao;
import com.pongo.pongoedu.domain.repository.AgendamentoRepository;
import com.pongo.pongoedu.domain.repository.MovimentacaoEstoqueRepository;
import com.pongo.pongoedu.domain.repository.NotificacaoRepository;
import com.pongo.pongoedu.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final NotificadorPort notificador;

    @Transactional
    public Notificacao registrar(Usuario destinatario, TipoNotificacao tipo, String assunto, String mensagem) {
        var notificacao = Notificacao.builder()
                .destinatario(destinatario)
                .tipo(tipo)
                .assunto(assunto)
                .mensagem(mensagem)
                .build();
        notificacaoRepository.save(notificacao);
        despachar(notificacao);
        return notificacao;
    }

    @Transactional
    public void notificarPerfil(Perfil perfil, TipoNotificacao tipo, String assunto, String mensagem) {
        usuarioRepository.findByPerfil(perfil).stream()
                .filter(usuario -> Boolean.TRUE.equals(usuario.getAtivo()))
                .forEach(usuario -> registrar(usuario, tipo, assunto, mensagem));
    }

    @Transactional(readOnly = true)
    public List<NotificacaoResponse> listarDoUsuario(Long usuarioId) {
        return notificacaoRepository.findByDestinatarioIdOrderByDataGeracaoDesc(usuarioId).stream()
                .map(NotificacaoResponse::de)
                .toList();
    }

    /**
     * RF18 - ciclo semanal: materiais recebidos na semana e datas importantes da agenda.
     */
    @Transactional
    public int executarCicloSemanal() {
        var inicio = LocalDate.now().minusDays(7);
        var fim = LocalDate.now();

        var entradas = movimentacaoRepository.findByTipoAndDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(
                TipoMovimentacao.ENTRADA, inicio.atStartOfDay(), fim.atTime(LocalTime.MAX));
        var agendamentos = agendamentoRepository.findByDataBetweenOrderByDataAscHoraInicioAsc(fim, fim.plusDays(7));

        var novidades = entradas.isEmpty()
                ? "Nenhum material novo recebido nesta semana."
                : entradas.stream()
                .map(mov -> "- " + mov.getProduto().getNome() + " (+" + mov.getQuantidade()
                        + ", lote " + mov.getLote().getCodigo() + ")")
                .collect(Collectors.joining("\n"));

        var agenda = agendamentos.isEmpty()
                ? "Nenhuma pratica agendada para os proximos 7 dias."
                : agendamentos.stream()
                .map(ag -> "- " + ag.getData() + " " + ag.getHoraInicio() + " | " + ag.getLaboratorio().getNome()
                        + " | " + (ag.getRoteiro() != null ? ag.getRoteiro().getTitulo() : "sem roteiro"))
                .collect(Collectors.joining("\n"));

        var mensagem = "Novidades de estoque:\n" + novidades + "\n\nAgenda do laboratorio:\n" + agenda;

        var destinatarios = usuarioRepository.findByAtivoTrue();
        destinatarios.forEach(usuario ->
                registrar(usuario, TipoNotificacao.NOVIDADES_SEMANAIS, "PongoEdu - novidades da semana", mensagem));
        return destinatarios.size();
    }

    /**
     * RF18 - lembrete na vespera de cada pratica agendada.
     */
    @Transactional
    public int enviarLembretesDaVespera() {
        var amanha = LocalDate.now().plusDays(1);
        var agendamentos = agendamentoRepository.findByDataAndSituacao(amanha, SituacaoAgendamento.AGENDADO);

        int enviados = 0;
        for (var agendamento : agendamentos) {
            var mensagem = "Lembrete: pratica em " + agendamento.getData() + " as " + agendamento.getHoraInicio()
                    + " no " + agendamento.getLaboratorio().getNome()
                    + (agendamento.getRoteiro() != null ? " | roteiro: " + agendamento.getRoteiro().getTitulo() : "");

            registrar(agendamento.getProfessor(), TipoNotificacao.LEMBRETE_PRATICA,
                    "PongoEdu - pratica amanha", mensagem);
            enviados++;

            for (var auxiliar : usuarioRepository.findByPerfil(Perfil.AUXILIAR_LABORATORIO)) {
                if (Boolean.TRUE.equals(auxiliar.getAtivo())) {
                    registrar(auxiliar, TipoNotificacao.LEMBRETE_PRATICA, "PongoEdu - pratica amanha", mensagem);
                    enviados++;
                }
            }
        }
        return enviados;
    }

    /**
     * Excecao 3b do RF18: reprocessa os envios que falharam no ciclo anterior.
     */
    @Transactional
    public int reprocessarPendentes() {
        var pendentes = notificacaoRepository.findBySituacaoOrderByDataGeracaoAsc(SituacaoNotificacao.FALHA);
        pendentes.forEach(this::despachar);
        return pendentes.size();
    }

    private void despachar(Notificacao notificacao) {
        try {
            notificador.enviar(notificacao);
            notificacao.setSituacao(SituacaoNotificacao.ENVIADA);
            notificacao.setDataEnvio(java.time.LocalDateTime.now());
            notificacao.setErro(null);
        } catch (RuntimeException ex) {
            notificacao.setSituacao(SituacaoNotificacao.FALHA);
            notificacao.setErro(ex.getMessage());
        } finally {
            notificacao.setTentativas(notificacao.getTentativas() + 1);
            notificacaoRepository.save(notificacao);
        }
    }
}
