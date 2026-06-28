package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.cardapio.model.Prato;
import com.raizesdonordeste.app.domain.cardapio.repository.PratoRepository;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Pagina;
import com.raizesdonordeste.app.domain.comum.model.Paginacao;
import com.raizesdonordeste.app.infra.persistence.jpa.PratoJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.PratoPersistenceMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Pagina<Prato> obterPaginaDeAtivosEDisponiveisPorUnidadeId(Id unidadeId, Paginacao paginacao) {
        Pageable pageable = PageRequest.of(paginacao.page() - 1, paginacao.size());
        Page<Prato> page = pratoJpaRepository.findByUnidadeIdAndAtivoAndDisponivel(
                unidadeId.id(), true, true, pageable).map(mapper::toDomain);
        return toPagina(page);
    }

    private Pagina<Prato> toPagina(Page<Prato> page) {
        return new Pagina<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
