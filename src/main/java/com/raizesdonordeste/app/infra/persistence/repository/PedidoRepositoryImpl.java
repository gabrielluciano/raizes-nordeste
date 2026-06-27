package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.pedido.model.Pedido;
import com.raizesdonordeste.app.domain.pedido.repository.PedidoRepository;
import com.raizesdonordeste.app.infra.persistence.entity.ItemPedidoEntity;
import com.raizesdonordeste.app.infra.persistence.entity.PedidoEntity;
import com.raizesdonordeste.app.infra.persistence.jpa.ItemPedidoJpaRepository;
import com.raizesdonordeste.app.infra.persistence.jpa.PedidoJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.PedidoPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public void atualizar(Pedido pedido) {
        PedidoEntity entity = mapper.toEntity(pedido);
        pedidoJpaRepository.save(entity);
    }

    @Override
    public Optional<Pedido> obterPorId(Id id) {
        List<ItemPedidoEntity> itemPedidoEntities = itemPedidoJpaRepository.findAllByPedidoId(id.id());
        return pedidoJpaRepository.findById(id.id())
                .map(pedidoEntity -> mapper.toDomain(pedidoEntity, itemPedidoEntities));
    }
}
