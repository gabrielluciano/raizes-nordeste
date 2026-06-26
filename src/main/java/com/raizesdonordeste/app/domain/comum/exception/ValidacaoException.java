package com.raizesdonordeste.app.domain.comum.exception;

public class ValidacaoException extends DominioException {

    public ValidacaoException(String message) {
        super("VALIDACAO", message);
    }
}
