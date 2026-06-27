package com.raizesdonordeste.app.domain.pagamento.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.pedido.model.StatusPedido;

public record ResultadoProcessamentoPagamento(
        Id id,
        StatusPagamento statusPagamento,
        StatusPedido statusPedido,
        String motivoRecusa
) {
}
