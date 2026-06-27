package com.raizesdonordeste.app.api.dto;

import com.raizesdonordeste.app.domain.pagamento.model.StatusPagamento;

import java.time.LocalDateTime;

public record PagamentoAssincronoResponse(
        String id,
        StatusPagamento statusPagamento,
        String qrCode,
        LocalDateTime qrCodeValidoAte
) {
}
