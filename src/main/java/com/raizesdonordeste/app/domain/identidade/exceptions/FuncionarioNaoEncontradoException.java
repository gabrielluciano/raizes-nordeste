package com.raizesdonordeste.app.domain.identidade.exceptions;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.comum.model.Id;

public class FuncionarioNaoEncontradoException extends ValidacaoException {

    public FuncionarioNaoEncontradoException(Id funcionarioId) {
        super("funcionário de id '%s' não encontrado.".formatted(funcionarioId));
    }

    public FuncionarioNaoEncontradoException(String message) {
        super(message);
    }
}
