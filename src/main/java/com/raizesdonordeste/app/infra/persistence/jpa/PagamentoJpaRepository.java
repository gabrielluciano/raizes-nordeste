package com.raizesdonordeste.app.infra.persistence.jpa;

import com.raizesdonordeste.app.infra.persistence.entity.PagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PagamentoJpaRepository extends JpaRepository<PagamentoEntity, UUID> {

    Optional<PagamentoEntity> findByIdTransacaoGateway(String idTransacaoGateway);

    Optional<PagamentoEntity> findByIdempotencyKey(String idempotencyKey);
}
