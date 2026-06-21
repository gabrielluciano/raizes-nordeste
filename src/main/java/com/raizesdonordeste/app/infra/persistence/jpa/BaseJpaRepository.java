package com.raizesdonordeste.app.infra.persistence.jpa;

import com.raizesdonordeste.app.infra.persistence.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BaseJpaRepository extends JpaRepository<BaseEntity, UUID> {

    Optional<BaseEntity> findBySlug(String slug);
}
