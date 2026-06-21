package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.identidade.model.Base;
import com.raizesdonordeste.app.domain.identidade.repository.BaseRepository;
import com.raizesdonordeste.app.infra.persistence.jpa.BaseJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.BasePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BaseRepositoryImpl implements BaseRepository {

    private final BaseJpaRepository baseJpaRepository;
    private final BasePersistenceMapper mapper;

    @Override
    public Optional<Base> obterPorSlug(String slug) {
        return baseJpaRepository.findBySlug(slug).map(mapper::toDomain);
    }
}
