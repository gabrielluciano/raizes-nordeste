package com.raizesdonordeste.app.api.dto;

import com.raizesdonordeste.app.domain.pagamento.model.StatusPagamento;
import com.raizesdonordeste.app.domain.pedido.model.StatusPedido;

public record PagamentoSincronoResponse(
        String id,
        StatusPagamento statusPagamento,
        StatusPedido statusPedido,
        String motivoRecusa
) {
}
