package com.raizesdonordeste.app.infra.mapper;

import com.raizesdonordeste.app.api.dto.PedidoResponse;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.pedido.model.ItemPedido;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoResponseMapper {

    public PedidoResponse toResponse(Pedido pedido) {
        List<PedidoResponse.ItemPedidoResponse> itens = toListaItemResponse(pedido);

        return new PedidoResponse(
                idToString(pedido.getId()),
                idToString(pedido.getUnidadeId()),
                idToString(pedido.getClienteId()),
                idToString(pedido.getClienteFidelidadeId()),
                idToString(pedido.getFuncionarioId()),
                pedido.getNomeCliente(),
                pedido.getCanal(),
                pedido.getStatus(),
                pedido.isPickup(),
                pedido.getHorarioPedido(),
                pedido.getHorarioPreparo(),
                pedido.isConsentimentoFidelizacao(),
                itens,
                pedido.getValorTotal().centavos(),
                pedido.getValorDescontoPromocao().centavos(),
                pedido.getValorDescontoPontos().centavos(),
                pedido.getValorFinal().centavos()
        );
    }

    private List<PedidoResponse.ItemPedidoResponse> toListaItemResponse(Pedido pedido) {
        return pedido.getItensPedido().stream()
                .map(this::toItemResponse)
                .toList();
    }

    private PedidoResponse.ItemPedidoResponse toItemResponse(ItemPedido item) {
        return new PedidoResponse.ItemPedidoResponse(
                idToString(item.id()),
                idToString(item.pratoId()),
                item.quantidade(),
                item.precoUnitario().centavos()
        );
    }

    private String idToString(Id id) {
        return id == null ? null : id.toString();
    }
}
