package com.raizesdonordeste.app.infra.persistence.jpa;

import com.raizesdonordeste.app.infra.persistence.entity.PratoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface PratoJpaRepository extends JpaRepository<PratoEntity, UUID> {

    Set<PratoEntity> findByIdIn(Set<UUID> ids);

    Page<PratoEntity> findByUnidadeIdAndAtivoAndDisponivel(UUID unidadeId, boolean ativo, boolean disponivel, Pageable pageable);
}
