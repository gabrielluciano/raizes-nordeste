package com.raizesdonordeste.app.domain.pedido.model;

import java.util.Set;

public enum StatusPedido {

    PAGAMENTO_PENDENTE,
    AGUARDANDO_PREPARO,
    EM_PREPARO,
    PRONTO,
    CONCLUIDO,
    CANCELADO;

    public static final Set<StatusPedido> STATUS_FINAIS = Set.of(CONCLUIDO, CANCELADO);
}
