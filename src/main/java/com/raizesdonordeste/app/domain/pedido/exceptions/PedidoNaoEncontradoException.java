package com.raizesdonordeste.app.domain.pedido.exceptions;

import com.raizesdonordeste.app.domain.comum.exception.ValidacaoException;
import com.raizesdonordeste.app.domain.comum.model.Id;

public class PedidoNaoEncontradoException extends ValidacaoException {

    public PedidoNaoEncontradoException(Id pedidoId) {
        super("pedido de id '%s' não encontrado.".formatted(pedidoId));
    }
}
