package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.request.AgendamentoRequest;
import com.pongo.pongoedu.application.dto.response.AgendamentoResponse;
import com.pongo.pongoedu.application.dto.response.DisponibilidadeResponse;
import com.pongo.pongoedu.domain.entity.Agendamento;
import com.pongo.pongoedu.domain.entity.Usuario;
import com.pongo.pongoedu.domain.enums.Perfil;
import com.pongo.pongoedu.domain.enums.SituacaoAgendamento;
import com.pongo.pongoedu.domain.enums.TipoNotificacao;
import com.pongo.pongoedu.domain.exception.ConflitoException;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private static final LocalTime ABERTURA = LocalTime.of(7, 0);
    private static final LocalTime FECHAMENTO = LocalTime.of(22, 0);

    private final AgendamentoRepository agendamentoRepository;
    private final LaboratorioService laboratorioService;
    private final RoteiroService roteiroService;
    private final NotificacaoService notificacaoService;

    /**
     * RF14 - o professor agenda a pratica, vinculando um roteiro a reserva.
     */
    @Transactional
    public AgendamentoResponse agendar(AgendamentoRequest request, Usuario professor) {
        if (!request.horaInicio().isBefore(request.horaFim())) {
            throw new DomainException("O horario de inicio deve ser anterior ao horario de fim.");
        }

        var laboratorio = laboratorioService.buscarEntidade(request.laboratorioId());

        // Excecao 2a/2b do RF14: horario ja reservado.
        var conflitos = agendamentoRepository.buscarConflitos(laboratorio.getId(), request.data(),
                request.horaInicio(), request.horaFim(), SituacaoAgendamento.CANCELADO, null);
        if (!conflitos.isEmpty()) {
            throw new ConflitoException("O horario escolhido ja esta reservado para este laboratorio.");
        }

        var agendamento = Agendamento.builder()
                .professor(professor)
                .laboratorio(laboratorio)
                .turma(request.turma())
                .data(request.data())
                .horaInicio(request.horaInicio())
                .horaFim(request.horaFim())
                .situacao(SituacaoAgendamento.AGENDADO)
                .observacao(request.observacao())
                .build();

        if (request.roteiroId() != null) {
            // Excecao 4a/4b do RF14: material indisponivel apenas sinaliza, nao bloqueia o agendamento.
            agendamento.setRoteiro(roteiroService.buscarEntidade(request.roteiroId()));
        }

        agendamentoRepository.save(agendamento);

        notificacaoService.notificarPerfil(Perfil.AUXILIAR_LABORATORIO, TipoNotificacao.AGENDAMENTO,
                "PongoEdu - nova pratica agendada",
                professor.getNome() + " agendou uma pratica em " + laboratorio.getNome()
                        + " no dia " + request.data() + " das " + request.horaInicio() + " as " + request.horaFim() + ".");

        return AgendamentoResponse.de(agendamento);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listar(Usuario usuario) {
        List<Agendamento> agendamentos = usuario.isProfessor()
                ? agendamentoRepository.findByProfessorIdOrderByDataDescHoraInicioDesc(usuario.getId())
                : agendamentoRepository.findAllByOrderByDataDescHoraInicioDesc();
        return agendamentos.stream().map(AgendamentoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return agendamentoRepository.findByDataBetweenOrderByDataAscHoraInicioAsc(inicio, fim).stream()
                .map(AgendamentoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscar(Long id) {
        return AgendamentoResponse.de(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public DisponibilidadeResponse consultarDisponibilidade(Long laboratorioId, LocalDate data) {
        var ocupacao = agendamentoRepository.buscarOcupacaoDoDia(laboratorioId, data, SituacaoAgendamento.CANCELADO);

        var ocupados = ocupacao.stream()
                .map(ag -> new DisponibilidadeResponse.Intervalo(ag.getHoraInicio(), ag.getHoraFim()))
                .toList();

        var livres = calcularIntervalosLivres(ocupacao.stream()
                .map(ag -> new DisponibilidadeResponse.Intervalo(ag.getHoraInicio(), ag.getHoraFim()))
                .sorted(Comparator.comparing(DisponibilidadeResponse.Intervalo::horaInicio))
                .toList());

        return new DisponibilidadeResponse(
                !livres.isEmpty(),
                livres.isEmpty() ? "Nao ha horarios livres neste dia." : "Horarios livres encontrados.",
                ocupados,
                livres
        );
    }

    /**
     * RF15 - o professor gerencia (reagenda/cancela) um agendamento proprio, desde que ainda nao preparado.
     */
    @Transactional
    public AgendamentoResponse gerenciar(Long id, AgendamentoRequest request, Usuario professor) {
        var agendamento = buscarEntidade(id);
        if (!agendamento.getProfessor().getId().equals(professor.getId())) {
            throw new DomainException("O agendamento pertence a outro professor.");
        }

        // Excecao 4a/4b do RF15: pratica ja preparada ou realizada nao pode ser alterada pelo professor.
        if (!agendamento.podeSerAlteradoPeloProfessor()) {
            throw new DomainException("A pratica ja foi preparada ou ja ocorreu. "
                    + "Entre em contato com o auxiliar de laboratorio para qualquer ajuste.");
        }

        var laboratorio = laboratorioService.buscarEntidade(request.laboratorioId());
        var conflitos = agendamentoRepository.buscarConflitos(laboratorio.getId(), request.data(),
                request.horaInicio(), request.horaFim(), SituacaoAgendamento.CANCELADO, id);
        if (!conflitos.isEmpty()) {
            throw new ConflitoException("O horario escolhido ja esta reservado para este laboratorio.");
        }

        agendamento.setLaboratorio(laboratorio);
        agendamento.setTurma(request.turma());
        agendamento.setData(request.data());
        agendamento.setHoraInicio(request.horaInicio());
        agendamento.setHoraFim(request.horaFim());
        agendamento.setObservacao(request.observacao());
        if (request.roteiroId() != null) {
            agendamento.setRoteiro(roteiroService.buscarEntidade(request.roteiroId()));
        }

        var salvo = agendamentoRepository.save(agendamento);

        notificacaoService.notificarPerfil(Perfil.AUXILIAR_LABORATORIO, TipoNotificacao.AGENDAMENTO,
                "PongoEdu - agendamento alterado",
                professor.getNome() + " alterou a pratica agendada para " + request.data() + ".");

        return AgendamentoResponse.de(salvo);
    }

    @Transactional
    public AgendamentoResponse cancelar(Long id, Usuario professor) {
        var agendamento = buscarEntidade(id);
        if (!agendamento.getProfessor().getId().equals(professor.getId())) {
            throw new DomainException("O agendamento pertence a outro professor.");
        }
        if (!agendamento.podeSerAlteradoPeloProfessor()) {
            throw new DomainException("A pratica ja foi preparada ou ja ocorreu e nao pode ser cancelada pelo professor.");
        }
        agendamento.setSituacao(SituacaoAgendamento.CANCELADO);
        return AgendamentoResponse.de(agendamentoRepository.save(agendamento));
    }

    public Agendamento buscarEntidade(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Agendamento", id));
    }

    private List<DisponibilidadeResponse.Intervalo> calcularIntervalosLivres(
            List<DisponibilidadeResponse.Intervalo> ocupadosOrdenados) {
        var livres = new ArrayList<DisponibilidadeResponse.Intervalo>();
        var cursor = ABERTURA;

        for (var ocupado : ocupadosOrdenados) {
            if (cursor.isBefore(ocupado.horaInicio())) {
                livres.add(new DisponibilidadeResponse.Intervalo(cursor, ocupado.horaInicio()));
            }
            if (ocupado.horaFim().isAfter(cursor)) {
                cursor = ocupado.horaFim();
            }
        }
        if (cursor.isBefore(FECHAMENTO)) {
            livres.add(new DisponibilidadeResponse.Intervalo(cursor, FECHAMENTO));
        }
        return livres;
    }
}
