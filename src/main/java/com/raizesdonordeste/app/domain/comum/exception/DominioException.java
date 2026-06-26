package com.raizesdonordeste.app.domain.comum.exception;

import lombok.Getter;

public class DominioException extends RuntimeException {

    @Getter
    private final String codigo;

    public DominioException(String codigo, String mensagem) {
        super(mensagem);
        this.codigo = codigo;
    }
}
