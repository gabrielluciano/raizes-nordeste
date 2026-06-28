package com.raizesdonordeste.app.domain.pedido.model;

import com.raizesdonordeste.app.domain.comum.model.Id;

public record AvancarStatusPedidoComando(Id contaId, Id pedidoId) {
}
