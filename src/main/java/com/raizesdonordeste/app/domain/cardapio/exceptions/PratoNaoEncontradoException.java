package com.raizesdonordeste.app.domain.cardapio.exceptions;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.comum.model.Id;

public class PratoNaoEncontradoException extends ValidacaoException {
    public PratoNaoEncontradoException(Id pratoId) {
        super("prato de id '%s' não encontrado.".formatted(pratoId));
    }
}
