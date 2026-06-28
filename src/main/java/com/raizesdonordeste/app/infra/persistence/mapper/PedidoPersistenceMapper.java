package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.pedido.model.CanalPedido;
import com.raizesdonordeste.app.domain.pedido.model.ItemPedido;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.model.StatusPedido;
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
                Id.toUUID(domain.getClienteFidelidadeId()),
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
                        item.id().id(),
                        pedidoId,
                        item.pratoId().id(),
                        item.quantidade(),
                        item.precoUnitario().centavos()
                ))
                .toList();
    }

    public Pedido toDomain(PedidoEntity entity, List<ItemPedidoEntity> itens) {
        return Pedido.builder()
                .id(new Id(entity.getId()))
                .unidadeId(new Id(entity.getUnidadeId()))
                .clienteId(Id.fromUUID(entity.getClienteId()))
                .clienteFidelidadeId(Id.fromUUID(entity.getClienteFidelidadeId()))
                .funcionarioId(Id.fromUUID(entity.getFuncionarioId()))
                .nomeCliente(entity.getNomeCliente())
                .canal(CanalPedido.valueOf(entity.getCanal()))
                .status(StatusPedido.valueOf(entity.getStatus()))
                .pickup(entity.isPickup())
                .horarioPedido(entity.getHorarioPedido())
                .horarioPreparo(entity.getHorarioPreparo())
                .consentimentoFidelizacao(entity.isConsentimentoFidelizacao())
                .valorTotal(new Dinheiro(entity.getValorTotalCentavos()))
                .valorDescontoPromocao(new Dinheiro(entity.getValorDescontoPromocaoCentavos()))
                .valorDescontoPontos(new Dinheiro(entity.getValorDescontoPontosCentavos()))
                .valorFinal(new Dinheiro(entity.getValorFinalCentavos()))
                .itens(toDomain(itens))
                .build();
    }

    private List<ItemPedido> toDomain(List<ItemPedidoEntity> itens) {
        return itens.stream()
                .map(item -> ItemPedido.builder()
                        .id(new Id(item.getId()))
                        .pratoId(new Id(item.getPratoId()))
                        .quantidade(item.getQuantidade())
                        .precoUnitario(new Dinheiro(item.getPrecoUnitarioCentavos()))
                        .build())
                .toList();
    }
}
