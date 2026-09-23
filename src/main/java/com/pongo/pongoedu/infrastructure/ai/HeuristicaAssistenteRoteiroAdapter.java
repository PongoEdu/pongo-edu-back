package com.pongo.pongoedu.infrastructure.ai;

import com.pongo.pongoedu.application.dto.request.SugestaoRoteiroRequest;
import com.pongo.pongoedu.application.dto.response.SugestaoRoteiroResponse;
import com.pongo.pongoedu.application.port.AssistenteRoteiroPort;
import com.pongo.pongoedu.domain.entity.Produto;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * RF17 - implementacao de referencia do apoio de IA na elaboracao de roteiros.
 * Seleciona materiais disponiveis em estoque compativeis com o tema informado.
 * Pode ser substituida por uma integracao real com a API da Anthropic (Claude)
 * mantendo o mesmo contrato de {@link AssistenteRoteiroPort}.
 */
@Component
public class HeuristicaAssistenteRoteiroAdapter implements AssistenteRoteiroPort {

    private static final int MAX_MATERIAIS_SUGERIDOS = 6;

    @Override
    public SugestaoRoteiroResponse sugerirRoteiro(SugestaoRoteiroRequest pedido, List<Produto> materiaisDisponiveis) {
        var selecionados = materiaisDisponiveis.stream()
                .sorted(Comparator.comparing(Produto::getQuantidadeEstoque).reversed())
                .limit(MAX_MATERIAIS_SUGERIDOS)
                .map(produto -> new SugestaoRoteiroResponse.RoteiroMaterialSugerido(
                        produto.getId(),
                        produto.getNome(),
                        Math.min(2, produto.getQuantidadeEstoque()),
                        produto.getQuantidadeEstoque()
                ))
                .toList();

        var titulo = "Pratica sobre " + pedido.tema();
        var objetivo = pedido.objetivo() != null && !pedido.objetivo().isBlank()
                ? pedido.objetivo()
                : "Explorar de forma pratica o tema \"" + pedido.tema() + "\" com os materiais disponiveis no laboratorio.";

        var procedimento = new StringBuilder("Procedimento sugerido:\n");
        int passo = 1;
        for (var material : selecionados) {
            procedimento.append(passo++).append(". Utilizar ").append(material.produtoNome())
                    .append(" (quantidade sugerida: ").append(material.quantidadeNecessaria()).append(")\n");
        }
        procedimento.append(passo).append(". Registrar as observacoes dos estudantes e concluir a atividade.");

        return new SugestaoRoteiroResponse(
                titulo,
                objetivo,
                procedimento.toString(),
                45,
                selecionados,
                "Sugestao gerada automaticamente com base no estoque disponivel. Revise antes de publicar."
        );
    }
}
