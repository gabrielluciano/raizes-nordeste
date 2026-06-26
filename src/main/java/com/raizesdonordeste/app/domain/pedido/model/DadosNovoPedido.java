package com.raizesdonordeste.app.domain.pedido.model;

import com.raizesdonordeste.app.domain.cardapio.model.Promocao;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.organizacao.model.Unidade;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
public record DadosNovoPedido(
        Id id,
        Unidade unidade,
        Cliente clienteVinculado,
        Cliente clienteFidelidade,
        Id funcionarioId,
        String cpfCliente,
        String nomeCliente,
        CanalPedido canal,
        boolean pickup,
        LocalDateTime horarioPedido,
        LocalDateTime horarioPreparo,
        boolean consentimentoFidelizacao,
        List<ItemPedido> itens,
        Set<Promocao> promocoes
) {
}
