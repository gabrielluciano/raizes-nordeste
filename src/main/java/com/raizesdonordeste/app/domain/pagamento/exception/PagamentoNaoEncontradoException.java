package com.raizesdonordeste.app.domain.pagamento.exception;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.comum.model.Id;

public class PagamentoNaoEncontradoException extends ValidacaoException {

    private PagamentoNaoEncontradoException(String mensagem, Id pagamentoId) {
        super(mensagem.formatted(pagamentoId));
    }

    private PagamentoNaoEncontradoException(String mensagem, String idTransacaoGateway) {
        super(mensagem.formatted(idTransacaoGateway));
    }

    public static PagamentoNaoEncontradoException porId(Id id) {
        return new PagamentoNaoEncontradoException("pagamento de id '%s' não encontrado.", id);
    }

    public static PagamentoNaoEncontradoException porIdTransacaoGateway(String idTransacaoGateway) {
        return new PagamentoNaoEncontradoException("pagamento de idTransacaoGateway '%s' não encontrado.", idTransacaoGateway);
    }
}
