package com.raizesdonordeste.app.domain.pedido.model;

import com.raizesdonordeste.app.domain.comum.model.CPF;
import com.raizesdonordeste.app.domain.comum.model.Id;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record DadosNovoPedido(
        Id id,
        Id unidadeId,
        Id clienteId,
        Id funcionarioId,
        String cpfCliente,
        String nomeCliente,
        CanalPedido canal,
        boolean pickup,
        LocalDateTime horarioPedido,
        boolean consentimentoFidelizacao,
        List<ItemPedido> itens
) {
}
