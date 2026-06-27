package com.raizesdonordeste.app.domain.pagamento.model.gateway;

import java.time.LocalDateTime;

public record RespostaGatewayPix(String id, String qrCode, LocalDateTime qrCodeValidoAte) {
}
