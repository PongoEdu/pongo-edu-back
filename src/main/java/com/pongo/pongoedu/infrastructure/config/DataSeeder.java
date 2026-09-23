package com.pongo.pongoedu.infrastructure.config;

import com.pongo.pongoedu.domain.entity.*;
import com.pongo.pongoedu.domain.enums.*;
import com.pongo.pongoedu.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final LoteRepository loteRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final ProdutoRepository produtoRepository;
    private final RoteiroRepository roteiroRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("Banco ja possui dados. Seed ignorado.");
            return;
        }

        log.info("Banco vazio. Executando seed...");
        var senhaPadrao = passwordEncoder.encode("123456");

        var professor = usuarioRepository.save(Usuario.builder()
                .nome("Prof. Silva")
                .email("professor@pongoedu.com")
                .senha(senhaPadrao)
                .perfil(Perfil.PROFESSOR)
                .ativo(true)
                .build());

        var auxiliar = usuarioRepository.save(Usuario.builder()
                .nome("Aux. Maria")
                .email("auxiliar@pongoedu.com")
                .senha(senhaPadrao)
                .perfil(Perfil.AUXILIAR_LABORATORIO)
                .ativo(true)
                .build());

        var reagentes = categoriaRepository.save(Categoria.builder()
                .nome("Reagentes")
                .descricao("Substancias quimicas de uso em pratica")
                .permiteEmprestimo(false)
                .ativo(true)
                .build());

        var vidrarias = categoriaRepository.save(Categoria.builder()
                .nome("Vidrarias")
                .descricao("Materiais de vidro de laboratorio")
                .permiteEmprestimo(false)
                .ativo(true)
                .build());

        var equipamentos = categoriaRepository.save(Categoria.builder()
                .nome("Equipamentos")
                .descricao("Instrumentos e equipamentos de laboratorio")
                .permiteEmprestimo(true)
                .ativo(true)
                .build());

        var loteReagentes = loteRepository.save(Lote.builder()
                .codigo("LOTE-2026-001")
                .fornecedor("Quimica Distribuidora LTDA")
                .dataFabricacao(LocalDate.now().minusMonths(6))
                .dataValidade(LocalDate.now().plusMonths(18))
                .build());

        var loteVidrarias = loteRepository.save(Lote.builder()
                .codigo("LOTE-2026-002")
                .fornecedor("VidroLab Equipamentos")
                .dataFabricacao(LocalDate.now().minusYears(1))
                .dataValidade(null)
                .build());

        var loteVencendo = loteRepository.save(Lote.builder()
                .codigo("LOTE-2026-003")
                .fornecedor("Quimica Distribuidora LTDA")
                .dataFabricacao(LocalDate.now().minusMonths(11))
                .dataValidade(LocalDate.now().plusDays(15))
                .build());

        var acidoCloridrico = produtoRepository.save(Produto.builder()
                .codigo("REA-001")
                .nome("Acido Cloridrico 1M")
                .descricao("HCl 1M para praticas de titulacao")
                .categoria(reagentes)
                .lote(loteReagentes)
                .unidadeMedida(UnidadeMedida.MILILITRO)
                .quantidadeEstoque(500)
                .estoqueMinimo(100)
                .localizacao("Armario A, Prateleira 1")
                .ativo(true)
                .build());

        var bicarbonato = produtoRepository.save(Produto.builder()
                .codigo("REA-002")
                .nome("Bicarbonato de Sodio")
                .descricao("NaHCO3 em po")
                .categoria(reagentes)
                .lote(loteVencendo)
                .unidadeMedida(UnidadeMedida.GRAMA)
                .quantidadeEstoque(20)
                .estoqueMinimo(15)
                .localizacao("Armario A, Prateleira 1")
                .ativo(true)
                .build());

        var becker = produtoRepository.save(Produto.builder()
                .codigo("VID-001")
                .nome("Becker 250ml")
                .descricao("Vidro borossilicato")
                .categoria(vidrarias)
                .lote(loteVidrarias)
                .unidadeMedida(UnidadeMedida.UNIDADE)
                .quantidadeEstoque(30)
                .estoqueMinimo(10)
                .localizacao("Armario B, Prateleira 2")
                .ativo(true)
                .build());

        var microscopio = produtoRepository.save(Produto.builder()
                .codigo("EQP-001")
                .nome("Microscopio Optico")
                .descricao("Microscopio biologico 1000x")
                .categoria(equipamentos)
                .lote(loteVidrarias)
                .unidadeMedida(UnidadeMedida.UNIDADE)
                .quantidadeEstoque(5)
                .estoqueMinimo(1)
                .localizacao("Armario C")
                .ativo(true)
                .build());

        laboratorioRepository.save(Laboratorio.builder()
                .nome("Laboratorio de Ciencias 1")
                .descricao("Laboratorio principal para praticas de quimica e biologia")
                .capacidade(30)
                .ativo(true)
                .build());

        var vulcao = Roteiro.builder()
                .titulo("Vulcao de Bicarbonato")
                .objetivo("Simular uma erupcao vulcanica por meio de reacao acido-base")
                .procedimento("1. Montar um cone de argila\n2. Colocar bicarbonato dentro\n"
                        + "3. Adicionar corante\n4. Despejar acido diluido\n5. Observar a reacao")
                .tema("Reacoes quimicas")
                .disciplina("Quimica")
                .nivelEnsino(NivelEnsino.FUNDAMENTAL_II)
                .nivelSeguranca(NivelSeguranca.BAIXO)
                .tempoEstimadoMinutos(30)
                .professor(professor)
                .geradoPorIa(false)
                .publicado(true)
                .build();
        vulcao.adicionarMaterial(RoteiroMaterial.builder().produto(bicarbonato).quantidadeNecessaria(2).build());
        vulcao.adicionarMaterial(RoteiroMaterial.builder().produto(acidoCloridrico).quantidadeNecessaria(1).build());
        roteiroRepository.save(vulcao);

        log.info("Seed concluido com sucesso!");
        log.info("Produto com estoque acima do minimo: {}", microscopio.getNome());
    }
}
