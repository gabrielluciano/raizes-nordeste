package com.raizesdonordeste.app.infra.persistence.jpa;

import com.raizesdonordeste.app.infra.persistence.entity.ItemPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemPedidoJpaRepository extends JpaRepository<ItemPedidoEntity, UUID> {

    List<ItemPedidoEntity> findAllByPedidoId(UUID pedidoId);
}
