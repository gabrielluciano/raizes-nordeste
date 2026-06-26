package com.raizesdonordeste.app.infra.persistence.jpa;

import com.raizesdonordeste.app.infra.persistence.entity.MovimentacaoPontosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovimentacaoPontosJpaRepository extends JpaRepository<MovimentacaoPontosEntity, UUID> {
}
