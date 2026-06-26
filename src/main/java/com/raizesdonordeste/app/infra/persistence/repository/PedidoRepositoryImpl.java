package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import com.raizesdonordeste.app.infra.persistence.entity.PedidoEntity;
import com.raizesdonordeste.app.infra.persistence.jpa.ItemPedidoJpaRepository;
import com.raizesdonordeste.app.infra.persistence.jpa.PedidoJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.PedidoPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PedidoRepositoryImpl implements PedidoRepository {

    private final PedidoJpaRepository pedidoJpaRepository;
    private final ItemPedidoJpaRepository itemPedidoJpaRepository;
    private final PedidoPersistenceMapper mapper;

    @Override
    public void inserir(Pedido pedido) {
        PedidoEntity entity = mapper.toEntity(pedido);
        pedidoJpaRepository.save(entity);
        itemPedidoJpaRepository.saveAll(mapper.toItemEntities(pedido.getItensPedido(), entity.getId()));
    }
}
