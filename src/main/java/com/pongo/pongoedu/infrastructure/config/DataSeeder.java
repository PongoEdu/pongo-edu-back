package com.pongo.pongoedu.infrastructure.config;

import com.pongo.pongoedu.domain.entities.*;
import com.pongo.pongoedu.domain.enums.Role;
import com.pongo.pongoedu.domain.enums.StatusAtividade;
import com.pongo.pongoedu.domain.enums.TipoMaterial;
import com.pongo.pongoedu.infrastructure.persistence.jpa.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UsuarioJpaRepository usuarioRepo;
    private final ProfessorJpaRepository professorRepo;
    private final AlunoJpaRepository alunoRepo;
    private final AuxiliarJpaRepository auxiliarRepo;
    private final TurmaJpaRepository turmaRepo;
    private final MaterialJpaRepository materialRepo;
    private final AtividadeJpaRepository atividadeRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepo.count() > 0) {
            log.info("Banco já possui dados. Seed ignorado.");
            return;
        }

        log.info("Banco vazio. Executando seed...");

        // Senha padrão: 123456
        String senhaPadrao = passwordEncoder.encode("123456");

        // Usuários
        var usuProf = usuarioRepo.save(new Usuario(
                null, "Prof. Silva", "professor@pongoedu.com",
                senhaPadrao, Role.PROFESSOR, true, LocalDateTime.now()
        ));
        var usuAna = usuarioRepo.save(new Usuario(
                null, "Ana Santos", "ana@pongoedu.com",
                senhaPadrao, Role.ALUNO, true, LocalDateTime.now()
        ));
        var usuCarlos = usuarioRepo.save(new Usuario(
                null, "Carlos Lima", "carlos@pongoedu.com",
                senhaPadrao, Role.ALUNO, true, LocalDateTime.now()
        ));
        var usuAux = usuarioRepo.save(new Usuario(
                null, "Aux. Maria", "auxiliar@pongoedu.com",
                senhaPadrao, Role.AUXILIAR, true, LocalDateTime.now()
        ));

        // Professor
        var professor = new Professor();
        professor.setUsuario(usuProf);
        professor.setDepartamento("Ciências");
        professor = professorRepo.save(professor);

        // Alunos
        var ana = new Aluno();
        ana.setUsuario(usuAna);
        ana.setMatricula("2024001");
        ana.setXpTotal(1500);
        ana.setOfensivaDias(12);

        var carlos = new Aluno();
        carlos.setUsuario(usuCarlos);
        carlos.setMatricula("2024002");
        carlos.setXpTotal(1320);
        carlos.setOfensivaDias(7);

        ana = alunoRepo.save(ana);
        carlos = alunoRepo.save(carlos);

        // Auxiliar
        var auxiliar = new Auxiliar();
        auxiliar.setUsuario(usuAux);
        auxiliar.setSetor("Laboratório Química");
        auxiliarRepo.save(auxiliar);

        // Turmas
        var turma1 = new Turma();
        turma1.setNome("1º Ano A");
        turma1.setProfessor(professor);

        var turma2 = new Turma();
        turma2.setNome("2º Ano B");
        turma2.setProfessor(professor);

        var turma3 = new Turma();
        turma3.setNome("3º Ano A");
        turma3.setProfessor(professor);

        turma1 = turmaRepo.save(turma1);
        turma2 = turmaRepo.save(turma2);
        turma3 = turmaRepo.save(turma3);

        // Matricular alunos
        ana.setTurmas(List.of(turma3));
        carlos.setTurmas(List.of(turma2));
        alunoRepo.save(ana);
        alunoRepo.save(carlos);

        // Materiais
        materialRepo.saveAll(List.of(
                criarMaterial("Ácido Clorídrico", "HCl 1M", 15, 5, TipoMaterial.REAGENTE),
                criarMaterial("Béquer 250ml", "Vidro borossilicato", 30, 10, TipoMaterial.VIDRARIA),
                criarMaterial("Modelo Coração Humano", "Modelo anatômico em resina", 3, 1, TipoMaterial.MODELO_ANATOMICO),
                criarMaterial("Hidróxido de Sódio", "NaOH em pó", 8, 3, TipoMaterial.REAGENTE),
                criarMaterial("Tubo de Ensaio", "15ml", 50, 20, TipoMaterial.VIDRARIA)
        ));

        // Atividades
        var atv1 = new Atividade();
        atv1.setTitulo("Prática de Titulação");
        atv1.setDescricao("Determinar concentração de ácido");
        atv1.setProfessor(professor);
        atv1.setTurma(turma3);
        atv1.setXpRecompensa(200);
        atv1.setStatus(StatusAtividade.NAO_INICIADA);
        atv1.setDataEntrega(LocalDate.now().plusDays(1));

        var atv2 = new Atividade();
        atv2.setTitulo("Game Química N2");
        atv2.setDescricao("Quiz sobre ligações químicas");
        atv2.setProfessor(professor);
        atv2.setTurma(turma2);
        atv2.setXpRecompensa(300);
        atv2.setStatus(StatusAtividade.EM_PROGRESSO);
        atv2.setDataEntrega(LocalDate.now().plusDays(3));

        atividadeRepo.saveAll(List.of(atv1, atv2));

        log.info("Seed concluído com sucesso!");
    }

    private Material criarMaterial(String nome, String desc, int qtd, int min, TipoMaterial tipo) {
        var m = new Material();
        m.setNome(nome);
        m.setDescricao(desc);
        m.setQuantidade(qtd);
        m.setQuantidadeMinima(min);
        m.setTipo(tipo);
        return m;
    }
}