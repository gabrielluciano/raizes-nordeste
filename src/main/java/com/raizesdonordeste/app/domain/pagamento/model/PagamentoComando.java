package com.raizesdonordeste.app.domain.pagamento.model;

import com.raizesdonordeste.app.domain.comum.model.Id;

public record PagamentoComando(
        Id contaId,
        Id pedidoId,
        String idempotencyKey,
        FormaPagamento formaPagamento,
        String token
) {
}
