package com.raizesdonordeste.app.domain.pagamento.model.gateway;

import java.time.LocalDateTime;

public record RespostaGatewayCartao(String id, boolean recusado, LocalDateTime pagoEm, String motivoRecusa) {
}
