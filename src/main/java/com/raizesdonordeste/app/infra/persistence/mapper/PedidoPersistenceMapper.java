package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.pedido.model.ItemPedido;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.infra.persistence.entity.ItemPedidoEntity;
import com.raizesdonordeste.app.infra.persistence.entity.PedidoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class PedidoPersistenceMapper {

    public PedidoEntity toEntity(Pedido domain) {
        return new PedidoEntity(
                domain.getId().id(),
                domain.getUnidadeId().id(),
                Id.toUUID(domain.getClienteId()),
                Id.toUUID(domain.getFuncionarioId()),
                domain.getNomeCliente(),
                domain.getCanal().name(),
                domain.getStatus().name(),
                domain.isPickup(),
                domain.getHorarioPedido(),
                domain.getHorarioPreparo(),
                domain.isConsentimentoFidelizacao(),
                domain.getValorTotal().centavos(),
                domain.getValorDescontoPromocao().centavos(),
                domain.getValorDescontoPontos().centavos(),
                domain.getValorFinal().centavos()
        );
    }

    public List<ItemPedidoEntity> toItemEntities(List<ItemPedido> itens, UUID pedidoId) {
        return itens.stream()
                .map(item -> new ItemPedidoEntity(
                        item.getId().id(),
                        pedidoId,
                        item.getPratoId().id(),
                        item.getQuantidade(),
                        item.getPrecoUnitario().centavos()
                ))
                .toList();
    }
}
