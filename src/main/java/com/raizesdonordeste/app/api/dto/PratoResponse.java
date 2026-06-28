package com.raizesdonordeste.app.api.dto;

public record PratoResponse(
        String id,
        String unidadeId,
        String nome,
        String descricao,
        long preco
) {
}
