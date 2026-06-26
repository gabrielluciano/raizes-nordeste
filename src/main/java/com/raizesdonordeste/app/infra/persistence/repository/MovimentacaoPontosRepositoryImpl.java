package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.fidelidade.model.MovimentacaoPontos;
import com.raizesdonordeste.app.domain.fidelidade.repository.MovimentacaoPontosRepository;
import com.raizesdonordeste.app.infra.persistence.jpa.MovimentacaoPontosJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.MovimentacaoPontosPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovimentacaoPontosRepositoryImpl implements MovimentacaoPontosRepository {

    private final MovimentacaoPontosJpaRepository movimentacaoPontosJpaRepository;
    private final MovimentacaoPontosPersistenceMapper mapper;

    @Override
    public void inserir(MovimentacaoPontos movimentacaoPontos) {
        movimentacaoPontosJpaRepository.save(mapper.toEntity(movimentacaoPontos));
    }
}
