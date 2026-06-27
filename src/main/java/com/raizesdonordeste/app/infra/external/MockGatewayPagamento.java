package com.raizesdonordeste.app.infra.external;

import com.raizesdonordeste.app.domain.pagamento.exception.ErroPagamentoException;
import com.raizesdonordeste.app.domain.pagamento.model.Pagamento;
import com.raizesdonordeste.app.domain.pagamento.model.gateway.GatewayPagamento;
import com.raizesdonordeste.app.domain.pagamento.model.gateway.RespostaGatewayCartao;
import com.raizesdonordeste.app.domain.pagamento.model.gateway.RespostaGatewayPix;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MockGatewayPagamento implements GatewayPagamento {

    @Override
    public RespostaGatewayCartao processarCartao(Pagamento pagamento, String token) throws ErroPagamentoException {
        if ("RECUSAR".equals(token)) {
            return new RespostaGatewayCartao(
                    UUID.randomUUID().toString(),
                    true,
                    LocalDateTime.now(),
                    "Saldo insuficiente"
            );
        }

        if ("ERRO".equals(token)) {
            throw new ErroPagamentoException("Indisponibilidade no gateway de pagamento");
        }

        return new RespostaGatewayCartao(
                UUID.randomUUID().toString(),
                false,
                LocalDateTime.now(),
                null
        );
    }

    @Override
    public RespostaGatewayPix processarPix(Pagamento pagamento) throws ErroPagamentoException {
        return new RespostaGatewayPix(
                "pix-" + pagamento.getPedidoId(),
                "qr-code-base64",
                LocalDateTime.now().plusHours(1)
        );
    }
}
