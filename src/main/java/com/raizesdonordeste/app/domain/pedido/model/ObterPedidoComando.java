package com.raizesdonordeste.app.domain.pedido.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.Role;

public record ObterPedidoComando(
        Id pedidoId,
        Id contaId,
        Role role
) {
}
