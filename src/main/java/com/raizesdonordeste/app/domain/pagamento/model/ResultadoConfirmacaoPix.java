package com.raizesdonordeste.app.domain.pagamento.model;

import com.raizesdonordeste.app.domain.comum.model.Id;

public record ResultadoConfirmacaoPix(Id id, StatusPagamento statusPagamento) {
}
