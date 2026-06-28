package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.fidelidade.model.MovimentacaoPontos;
import com.raizesdonordeste.app.domain.fidelidade.model.TipoMovPontos;
import com.raizesdonordeste.app.infra.persistence.entity.MovimentacaoPontosEntity;
import org.springframework.stereotype.Component;

@Component
public class MovimentacaoPontosPersistenceMapper {

    public MovimentacaoPontosEntity toEntity(MovimentacaoPontos domain) {
        return new MovimentacaoPontosEntity(
                domain.id().id(),
                domain.clienteId().id(),
                domain.pedidoId().id(),
                domain.tipo().name(),
                domain.pontos(),
                domain.dataContabilizacao(),
                domain.dataExpiracao()
        );
    }

    public MovimentacaoPontos toDomain(MovimentacaoPontosEntity entity) {
        return new MovimentacaoPontos(
                Id.fromUUID(entity.getId()),
                Id.fromUUID(entity.getClienteId()),
                Id.fromUUID(entity.getPedidoId()),
                TipoMovPontos.valueOf(entity.getTipo()),
                entity.getPontos(),
                entity.getDataContabilizacao(),
                entity.getDataExpiracao()
        );
    }
}
