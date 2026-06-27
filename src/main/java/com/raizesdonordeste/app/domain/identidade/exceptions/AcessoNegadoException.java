package com.raizesdonordeste.app.domain.identidade.exceptions;

import com.raizesdonordeste.app.domain.comum.exception.DominioException;

public class AcessoNegadoException extends DominioException {
    public AcessoNegadoException(String message) {
        super("ACESSO_NEGADO", message);
    }
}
