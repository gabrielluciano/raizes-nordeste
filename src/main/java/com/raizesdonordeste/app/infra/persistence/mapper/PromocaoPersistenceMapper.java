package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.cardapio.model.Promocao;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.infra.persistence.entity.PromocaoEntity;
import org.springframework.stereotype.Component;

@Component
public class PromocaoPersistenceMapper {

    public Promocao toDomain(PromocaoEntity entity) {
        return Promocao.builder()
                .id(new Id(entity.getId()))
                .pratoId(new Id(entity.getPratoId()))
                .descricao(entity.getDescricao())
                .percentualDesconto(entity.getPercentualDesconto())
                .dataHoraInicio(entity.getDataHoraInicio())
                .dataHoraFim(entity.getDataHoraFim())
                .ativa(entity.isAtiva())
                .build();
    }
}
