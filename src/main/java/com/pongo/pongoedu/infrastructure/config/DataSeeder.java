package com.pongo.pongoedu.infrastructure.config;

import com.pongo.pongoedu.domain.entities.*;
import com.pongo.pongoedu.domain.enums.*;
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
    private final ReagenteJpaRepository reagenteJpaRepo;
    private final ExperimentoJpaRepository experimentoJpaRepo;

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

        // === Reagentes ===
        var bicarbonato = new Reagente();
        bicarbonato.setNome("Bicarbonato de Sódio");
        bicarbonato.setFormula("NaHCO₃");
        bicarbonato.setQuantidade(20);
        bicarbonato.setQuantidadeMinima(5);
        bicarbonato.setUnidade("g");
        bicarbonato.setLocalizacao("Armário A, Prateleira 1");

        var vinagre = new Reagente();
        vinagre.setNome("Vinagre");
        vinagre.setFormula("CH₃COOH");
        vinagre.setQuantidade(15);
        vinagre.setQuantidadeMinima(5);
        vinagre.setUnidade("ml");
        vinagre.setLocalizacao("Armário A, Prateleira 1");

        var corante = new Reagente();
        corante.setNome("Corante Alimentício");
        corante.setQuantidade(10);
        corante.setQuantidadeMinima(3);
        corante.setUnidade("ml");
        corante.setLocalizacao("Armário B, Prateleira 2");

        var detergente = new Reagente();
        detergente.setNome("Detergente");
        detergente.setQuantidade(8);
        detergente.setQuantidadeMinima(3);
        detergente.setUnidade("ml");
        detergente.setLocalizacao("Armário B, Prateleira 2");

        var repolho = new Reagente();
        repolho.setNome("Extrato de Repolho Roxo");
        repolho.setQuantidade(0);
        repolho.setQuantidadeMinima(5);
        repolho.setUnidade("ml");
        repolho.setLocalizacao("Geladeira");

        var sulfatoCobre = new Reagente();
        sulfatoCobre.setNome("Sulfato de Cobre");
        sulfatoCobre.setFormula("CuSO₄");
        sulfatoCobre.setQuantidade(12);
        sulfatoCobre.setQuantidadeMinima(5);
        sulfatoCobre.setUnidade("g");
        sulfatoCobre.setLocalizacao("Armário C, Prateleira 1");

        var ferro = new Reagente();
        ferro.setNome("Prego de Ferro");
        ferro.setQuantidade(30);
        ferro.setQuantidadeMinima(10);
        ferro.setUnidade("un");
        ferro.setLocalizacao("Gaveta 3");

        var fioCobre = new Reagente();
        fioCobre.setNome("Fio de Cobre");
        fioCobre.setQuantidade(0);
        fioCobre.setQuantidadeMinima(5);
        fioCobre.setUnidade("un");
        fioCobre.setLocalizacao("Gaveta 3");

        reagenteJpaRepo.saveAll(List.of(
                bicarbonato, vinagre, corante, detergente,
                repolho, sulfatoCobre, ferro, fioCobre
        ));

// === Experimentos ===
        var vulcao = new Experimento();
        vulcao.setNome("Vulcão de Bicarbonato");
        vulcao.setDescricao("Simulação de erupção vulcânica usando reação ácido-base");
        vulcao.setPassoAPasso("1. Monte um cone de argila\n2. Coloque bicarbonato dentro\n3. Adicione corante\n4. Despeje vinagre\n5. Observe a reação");
        vulcao.setTempoEstimado(30);
        vulcao.setSerie("6º ano");
        vulcao.setDisciplina("Química");
        vulcao.setNivelSeguranca(NivelSeguranca.BAIXO);
        vulcao.setEfeito(EfeitoVisual.ESPUMA);
        vulcao.setReagentes(List.of(bicarbonato, vinagre, corante));

        var indicador = new Experimento();
        indicador.setNome("Indicador de pH com Repolho Roxo");
        indicador.setDescricao("Usar extrato de repolho roxo como indicador natural de pH");
        indicador.setPassoAPasso("1. Ferva o repolho roxo\n2. Coe o extrato\n3. Separe em copos\n4. Adicione substâncias (vinagre, sabão, limão)\n5. Observe a mudança de cor");
        indicador.setTempoEstimado(45);
        indicador.setSerie("8º ano");
        indicador.setDisciplina("Química");
        indicador.setNivelSeguranca(NivelSeguranca.BAIXO);
        indicador.setEfeito(EfeitoVisual.COR);
        indicador.setReagentes(List.of(repolho, vinagre));

        var testeChama = new Experimento();
        testeChama.setNome("Teste de Chama");
        testeChama.setDescricao("Identificar metais pela cor da chama");
        testeChama.setPassoAPasso("1. Prepare soluções dos sais metálicos\n2. Mergulhe o fio de cobre na solução\n3. Leve à chama do bico de Bunsen\n4. Observe a cor da chama\n5. Compare com a tabela de cores");
        testeChama.setTempoEstimado(40);
        testeChama.setSerie("9º ano");
        testeChama.setDisciplina("Química");
        testeChama.setNivelSeguranca(NivelSeguranca.ALTO);
        testeChama.setEfeito(EfeitoVisual.FOGO);
        testeChama.setReagentes(List.of(sulfatoCobre, fioCobre));

        var oxirreducao = new Experimento();
        oxirreducao.setNome("Oxidação do Ferro");
        oxirreducao.setDescricao("Demonstrar reação de oxirredução com sulfato de cobre e ferro");
        oxirreducao.setPassoAPasso("1. Prepare solução de sulfato de cobre\n2. Mergulhe o prego de ferro\n3. Aguarde 15 minutos\n4. Observe o depósito de cobre no prego\n5. Discuta a troca de elétrons");
        oxirreducao.setTempoEstimado(35);
        oxirreducao.setSerie("9º ano");
        oxirreducao.setDisciplina("Química");
        oxirreducao.setNivelSeguranca(NivelSeguranca.MEDIO);
        oxirreducao.setEfeito(EfeitoVisual.COR);
        oxirreducao.setReagentes(List.of(sulfatoCobre, ferro));

        experimentoJpaRepo.saveAll(List.of(vulcao, indicador, testeChama, oxirreducao));

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