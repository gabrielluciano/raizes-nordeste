package com.raizesdonordeste.app.domain.cardapio.exceptions;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.comum.model.Id;

public class PratoInativoException extends ValidacaoException {
    public PratoInativoException(Id pratoId) {
        super("prato de id '%s' não está disponível.".formatted(pratoId));
    }
}
