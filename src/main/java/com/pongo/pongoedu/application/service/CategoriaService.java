package com.pongo.pongoedu.application.service;

import com.pongo.pongoedu.application.dto.request.CategoriaRequest;
import com.pongo.pongoedu.application.dto.response.CategoriaResponse;
import com.pongo.pongoedu.domain.entity.Categoria;
import com.pongo.pongoedu.domain.exception.ConflitoException;
import com.pongo.pongoedu.domain.exception.DomainException;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.CategoriaRepository;
import com.pongo.pongoedu.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public CategoriaResponse criar(CategoriaRequest request) {
        if (categoriaRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new ConflitoException("Ja existe uma categoria com o nome " + request.nome());
        }
        var categoria = categoriaRepository.save(Categoria.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .permiteEmprestimo(request.permiteEmprestimo())
                .ativo(true)
                .build());
        return CategoriaResponse.de(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar(Boolean somenteAtivas) {
        var categorias = Boolean.TRUE.equals(somenteAtivas)
                ? categoriaRepository.findByAtivoTrue()
                : categoriaRepository.findAll();
        return categorias.stream().map(CategoriaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscar(Long id) {
        return CategoriaResponse.de(buscarEntidade(id));
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {
        var categoria = buscarEntidade(id);
        boolean nomeMudou = !categoria.getNome().equalsIgnoreCase(request.nome());
        if (nomeMudou && categoriaRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new ConflitoException("Ja existe uma categoria com o nome " + request.nome());
        }
        categoria.setNome(request.nome());
        categoria.setDescricao(request.descricao());
        categoria.setPermiteEmprestimo(request.permiteEmprestimo());
        return CategoriaResponse.de(categoriaRepository.save(categoria));
    }

    /**
     * Excecao 4a/4b do RF04: categoria com produtos vinculados nao pode ser excluida.
     */
    @Transactional
    public void remover(Long id) {
        var categoria = buscarEntidade(id);
        if (produtoRepository.existsByCategoriaId(id)) {
            throw new DomainException("A categoria possui produtos vinculados. "
                    + "Reclassifique os produtos antes de excluir ou mantenha a categoria inativa.");
        }
        categoriaRepository.delete(categoria);
    }

    public Categoria buscarEntidade(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Categoria", id));
    }
}
