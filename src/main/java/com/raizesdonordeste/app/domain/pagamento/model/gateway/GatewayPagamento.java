package com.raizesdonordeste.app.domain.pagamento.model.gateway;

import com.raizesdonordeste.app.domain.pagamento.exception.ErroPagamentoException;
import com.raizesdonordeste.app.domain.pagamento.model.Pagamento;

public interface GatewayPagamento {

    RespostaGatewayCartao processarCartao(Pagamento pagamento, String token) throws ErroPagamentoException;

    RespostaGatewayPix processarPix(Pagamento pagamento) throws ErroPagamentoException;
}
