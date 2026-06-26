package com.raizesdonordeste.app.domain.identidade.exceptions;

import com.raizesdonordeste.app.domain.comum.exception.DominioException;

public abstract class NaoAutorizadoException extends DominioException {

    protected NaoAutorizadoException(String codigo, String mensagem) {
        super(codigo, mensagem);
    }
}
