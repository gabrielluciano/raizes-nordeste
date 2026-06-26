package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.fidelidade.model.RegrasFidelidade;
import com.raizesdonordeste.app.infra.persistence.entity.RegrasFidelidadeEntity;
import org.springframework.stereotype.Component;

@Component
public class RegrasFidelidadePersistenceMapper {

    public RegrasFidelidade toDomain(RegrasFidelidadeEntity entity) {
        return RegrasFidelidade.builder()
                .id(new Id(entity.getId()))
                .valorPorPonto(entity.getValorPorPonto())
                .acumuloPorCentavo(entity.getAcumuloPorCentavo())
                .validadePontosMeses(entity.getValidadePontosMeses())
                .tetoResgatePercentual(entity.getTetoResgatePercentual())
                .build();
    }
}
