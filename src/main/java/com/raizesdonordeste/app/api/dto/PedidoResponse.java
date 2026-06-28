package com.raizesdonordeste.app.api.dto;

import com.raizesdonordeste.app.domain.pedido.model.CanalPedido;
import com.raizesdonordeste.app.domain.pedido.model.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        String id,
        String unidadeId,
        String clienteId,
        String clienteFidelidadeId,
        String funcionarioId,
        String nomeCliente,
        CanalPedido canal,
        StatusPedido status,
        boolean pickup,
        LocalDateTime horarioPedido,
        LocalDateTime horarioPreparo,
        boolean consentimentoFidelizacao,
        List<ItemPedidoResponse> itens,
        long valorTotal,
        long valorDescontoPromocao,
        long valorDescontoPontos,
        long valorFinal
) {

    public record ItemPedidoResponse(
            String id,
            String pratoId,
            int quantidade,
            long precoUnitario
    ) {
    }
}
