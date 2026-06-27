package com.raizesdonordeste.app.domain.identidade.exceptions;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.comum.model.Id;

public class ClienteNaoEncontradoException extends ValidacaoException {

    public ClienteNaoEncontradoException(Id clienteId) {
        super("cliente de id '%s' não encontrado.".formatted(clienteId));
    }
}
