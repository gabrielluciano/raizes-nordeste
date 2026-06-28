package com.raizesdonordeste.app.domain.cardapio.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Paginacao;

public record ObterCardapioComando(
        Id unidadeId,
        Paginacao paginacao
) {
}
