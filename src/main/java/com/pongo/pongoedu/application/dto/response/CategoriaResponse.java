package com.pongo.pongoedu.application.dto.response;

import com.pongo.pongoedu.domain.entity.Categoria;

public record CategoriaResponse(
        Long id,
        String nome,
        String descricao,
        Boolean permiteEmprestimo,
        Boolean ativo
) {
    public static CategoriaResponse de(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getPermiteEmprestimo(),
                categoria.getAtivo()
        );
    }
}
