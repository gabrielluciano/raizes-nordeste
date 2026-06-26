package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.cardapio.model.Prato;
import com.raizesdonordeste.app.domain.cardapio.repository.PratoRepository;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.infra.persistence.jpa.PratoJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.PratoPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PratoRepositoryImpl implements PratoRepository {

    private final PratoJpaRepository pratoJpaRepository;
    private final PratoPersistenceMapper mapper;

    @Override
    public Set<Prato> obterPratosPorIds(Set<Id> ids) {
        Set<UUID> uuids = ids.stream().map(Id::id).collect(Collectors.toSet());
        return pratoJpaRepository.findByIdIn(uuids).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toSet());
    }
}
