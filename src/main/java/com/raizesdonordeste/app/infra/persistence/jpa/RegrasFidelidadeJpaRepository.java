package com.raizesdonordeste.app.infra.persistence.jpa;

import com.raizesdonordeste.app.infra.persistence.entity.RegrasFidelidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RegrasFidelidadeJpaRepository extends JpaRepository<RegrasFidelidadeEntity, UUID> {

    Optional<RegrasFidelidadeEntity> findByAtivaTrue();
}
