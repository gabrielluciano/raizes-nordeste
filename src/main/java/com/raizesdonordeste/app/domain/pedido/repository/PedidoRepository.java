package com.raizesdonordeste.app.domain.pedido.repository;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;

import java.util.Optional;

public interface PedidoRepository {

    void inserir(Pedido pedido);

    void atualizar(Pedido pedido);

    Optional<Pedido> obterPorId(Id id);
}
