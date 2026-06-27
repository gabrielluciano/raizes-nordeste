package com.raizesdonordeste.app.domain.pagamento.model;

import java.time.LocalDateTime;

public record ConfirmarPagamentoPixComando(
        String transacaoId,
        LocalDateTime pagoEm
) {
}
