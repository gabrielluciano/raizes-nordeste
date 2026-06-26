package com.raizesdonordeste.app.infra.mapper;

import com.raizesdonordeste.app.api.dto.CriarPedidoRequest;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.Role;
import com.raizesdonordeste.app.domain.pedido.model.CriarPedidoComando;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CriarPedidoComandoMapper {

    public CriarPedidoComando toComando(CriarPedidoRequest request, Id contaId, Role role) {
        List<CriarPedidoComando.ItemComando> itens = toItemComandoLista(request);

        return new CriarPedidoComando(
                contaId,
                role,
                request.getUnidadeId() == null ? null : Id.fromString(request.getUnidadeId()),
                request.getCpfCliente(),
                request.getNomeCliente(),
                request.getCanal(),
                request.isPickup(),
                request.getHorarioPedido(),
                request.getHorarioPreparo(),
                request.isConsentimentoFidelizacao(),
                request.getPontosDesejados(),
                itens
        );
    }

    private List<CriarPedidoComando.ItemComando> toItemComandoLista(CriarPedidoRequest request) {
        return request.getItens().stream()
                .map(item -> new CriarPedidoComando.ItemComando(
                        Id.fromString(item.getPratoId()), item.getQuantidade()))
                .toList();
    }
}
