package com.raizesdonordeste.app.infra.persistence.jpa;

import com.raizesdonordeste.app.infra.persistence.entity.ContaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContaJpaRepository extends JpaRepository<ContaEntity, UUID> {

    Optional<ContaEntity> findByEmail(String email);
}
