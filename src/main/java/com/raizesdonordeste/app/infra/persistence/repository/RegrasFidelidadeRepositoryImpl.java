package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.fidelidade.model.RegrasFidelidade;
import com.raizesdonordeste.app.domain.fidelidade.repository.RegrasFidelidadeRepository;
import com.raizesdonordeste.app.infra.persistence.jpa.RegrasFidelidadeJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.RegrasFidelidadePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RegrasFidelidadeRepositoryImpl implements RegrasFidelidadeRepository {

    private final RegrasFidelidadeJpaRepository regrasFidelidadeJpaRepository;
    private final RegrasFidelidadePersistenceMapper mapper;

    @Override
    public RegrasFidelidade obterAtiva() {
        return regrasFidelidadeJpaRepository.findByAtivaTrue()
                .map(mapper::toDomain)
                .orElseThrow(() -> new IllegalStateException("Nenhuma regra de fidelidade ativa encontrada."));
    }
}
