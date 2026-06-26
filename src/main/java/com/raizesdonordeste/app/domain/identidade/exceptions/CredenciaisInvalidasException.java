package com.raizesdonordeste.app.domain.identidade.exceptions;

public class CredenciaisInvalidasException extends NaoAutorizadoException {

    public CredenciaisInvalidasException() {
        super("CREDENCIAIS_INVALIDAS", "Credenciais inválidas");
    }
}
