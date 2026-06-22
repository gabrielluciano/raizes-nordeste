package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.organizacao.model.Unidade;
import com.raizesdonordeste.app.domain.organizacao.repository.UnidadeRepository;
import com.raizesdonordeste.app.infra.persistence.jpa.UnidadeJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.UnidadePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UnidadeRepositoryImpl implements UnidadeRepository {

    private final UnidadeJpaRepository unidadeJpaRepository;
    private final UnidadePersistenceMapper mapper;

    @Override
    public Optional<Unidade> obterPorId(Id id) {
        return unidadeJpaRepository.findById(id.id()).map(mapper::toDomain);
    }
}
