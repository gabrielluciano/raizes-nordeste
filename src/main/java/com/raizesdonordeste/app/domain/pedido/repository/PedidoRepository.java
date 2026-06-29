package com.raizesdonordeste.app.domain.pedido.repository;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Pagina;
import com.raizesdonordeste.app.domain.comum.model.Paginacao;
import com.raizesdonordeste.app.domain.pedido.model.CanalPedido;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;

import java.util.Optional;

public interface PedidoRepository {

    void inserir(Pedido pedido);

    void atualizar(Pedido pedido);

    Optional<Pedido> obterPorId(Id id);

    Pagina<Pedido> listarTodos(Paginacao paginacao);

    Pagina<Pedido> listarPorCanal(CanalPedido canalPedido, Paginacao paginacao);

    Pagina<Pedido> listarPorClienteId(Id clienteId, Paginacao paginacao);

    Pagina<Pedido> listarPorClienteIdECanal(Id clienteId, CanalPedido canalPedido, Paginacao paginacao);

    Pagina<Pedido> listarPorUnidadeId(Id unidadeId, Paginacao paginacao);

    Pagina<Pedido> listarPorUnidadeIdECanal(Id unidadeId, CanalPedido canalPedido, Paginacao paginacao);
}
