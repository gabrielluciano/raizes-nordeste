package com.raizesdonordeste.app.domain.pagamento.exception;

public class ErroPagamentoException extends Exception {
    public ErroPagamentoException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }

    public ErroPagamentoException(String mensagem) {
        super(mensagem);
    }
}
