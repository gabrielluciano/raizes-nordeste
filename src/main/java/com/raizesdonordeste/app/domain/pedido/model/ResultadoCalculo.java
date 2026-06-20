package com.raizesdonordeste.app.domain.pedido.model;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;

public record ResultadoCalculo(
        Dinheiro valorTotal,
        Dinheiro valorFinal,
        Dinheiro valorDescontoPromocional,
        Dinheiro valorDescontoPontos,
        long pontosConsumidos) {
}
