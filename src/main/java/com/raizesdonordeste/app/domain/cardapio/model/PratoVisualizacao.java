package com.raizesdonordeste.app.domain.cardapio.model;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;

public record PratoVisualizacao(
        Id id,
        Id unidadeId,
        String nome,
        String descricao,
        Dinheiro preco
) {

    public static PratoVisualizacao fromPrato(Prato prato) {
        return new PratoVisualizacao(
                prato.getId(),
                prato.getUnidadeId(),
                prato.getNome(),
                prato.getDescricao(),
                prato.getPreco()
        );
    }
}
