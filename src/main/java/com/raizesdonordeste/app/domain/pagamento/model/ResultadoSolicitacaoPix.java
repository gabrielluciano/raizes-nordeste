package com.raizesdonordeste.app.domain.pagamento.model;

import com.raizesdonordeste.app.domain.comum.model.Id;

import java.time.LocalDateTime;

public record ResultadoSolicitacaoPix(
        Id id,
        StatusPagamento statusPagamento,
        String qrCode,
        LocalDateTime qrCodeValidoAte
) {
}
