package com.raizesdonordeste.app.domain.pedido.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Paginacao;
import com.raizesdonordeste.app.domain.identidade.model.Role;

public record ListarPedidosComando(
        Id contaId,
        Role role,
        CanalPedido canalPedido,
        Paginacao paginacao
) {
}
