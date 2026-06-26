package com.raizesdonordeste.app.domain.pedido.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.Role;

import java.time.LocalDateTime;
import java.util.List;

public record CriarPedidoComando(
        Id contaId,
        Role role,
        Id unidadeId,
        String cpfCliente,
        String nomeCliente,
        CanalPedido canal,
        boolean pickup,
        LocalDateTime horarioPedido,
        LocalDateTime horarioPreparo,
        boolean consentimentoFidelizacao,
        int pontosDesejados,
        List<ItemComando> itens
) {

    public record ItemComando(Id pratoId, int quantidade) {
    }
}
