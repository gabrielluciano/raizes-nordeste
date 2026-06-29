package com.raizesdonordeste.app.infra.persistence.jpa;

import com.raizesdonordeste.app.infra.persistence.entity.PedidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, UUID> {

    Page<PedidoEntity> findByCanal(String canal, Pageable pageable);

    Page<PedidoEntity> findByClienteId(UUID clienteId, Pageable pageable);

    Page<PedidoEntity> findByClienteIdAndCanal(UUID clienteId, String canal, Pageable pageable);

    Page<PedidoEntity> findByUnidadeId(UUID unidadeId, Pageable pageable);

    Page<PedidoEntity> findByUnidadeIdAndCanal(UUID unidadeId, String canal, Pageable pageable);
}
