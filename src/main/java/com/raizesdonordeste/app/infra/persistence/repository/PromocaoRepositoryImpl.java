package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.cardapio.model.Promocao;
import com.raizesdonordeste.app.domain.cardapio.repository.PromocaoRepository;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.infra.persistence.jpa.PromocaoJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.PromocaoPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PromocaoRepositoryImpl implements PromocaoRepository {

    private final PromocaoJpaRepository promocaoJpaRepository;
    private final PromocaoPersistenceMapper mapper;

    @Override
    public Set<Promocao> obterPromocoesAtivasParaPratos(Set<Id> pratoIds) {
        Set<UUID> uuids = pratoIds.stream().map(Id::id).collect(Collectors.toSet());
        return promocaoJpaRepository.findByAtivaIsTrueAndPratoIdIn(uuids).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toSet());
    }
}
