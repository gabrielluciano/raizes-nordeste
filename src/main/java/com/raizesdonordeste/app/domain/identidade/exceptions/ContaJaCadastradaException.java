package com.raizesdonordeste.app.domain.identidade.exceptions;

import com.raizesdonordeste.app.domain.comum.exception.DominioException;

public class ContaJaCadastradaException extends DominioException {

    public ContaJaCadastradaException() {
        super("CONTA_JA_CADASTRADA", "Conta já cadastrada.");
    }
}
