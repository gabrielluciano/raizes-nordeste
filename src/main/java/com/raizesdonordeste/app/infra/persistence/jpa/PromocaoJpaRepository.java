package com.raizesdonordeste.app.infra.persistence.jpa;

import com.raizesdonordeste.app.infra.persistence.entity.PromocaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface PromocaoJpaRepository extends JpaRepository<PromocaoEntity, UUID> {

    Set<PromocaoEntity> findByAtivaIsTrueAndPratoIdIn(Set<UUID> pratoIds);
}
