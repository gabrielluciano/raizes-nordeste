package com.raizesdonordeste.app.infra.mapper;

import com.raizesdonordeste.app.api.dto.PagamentoRequest;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.pagamento.model.PagamentoComando;
import org.springframework.stereotype.Component;

@Component
public class PagamentoComandoMapper {

    public PagamentoComando toComando(PagamentoRequest request, String contaId, String idempotencyKey) {
        return new PagamentoComando(
                Id.fromString(contaId),
                Id.fromString(request.getPedidoId()),
                idempotencyKey,
                request.getFormaPagamento(),
                request.getToken()
        );
    }
}
