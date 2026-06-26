package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.fidelidade.model.MovimentacaoPontos;
import com.raizesdonordeste.app.infra.persistence.entity.MovimentacaoPontosEntity;
import org.springframework.stereotype.Component;

@Component
public class MovimentacaoPontosPersistenceMapper {

    public MovimentacaoPontosEntity toEntity(MovimentacaoPontos domain) {
        return new MovimentacaoPontosEntity(
                domain.getId().id(),
                domain.getClienteId().id(),
                domain.getTipo().name(),
                domain.getPontos(),
                domain.getDataContabilizacao(),
                domain.getDataExpiracao()
        );
    }
}
