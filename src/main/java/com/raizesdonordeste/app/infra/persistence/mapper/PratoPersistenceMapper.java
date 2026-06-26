package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.cardapio.model.Prato;
import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.infra.persistence.entity.PratoEntity;
import org.springframework.stereotype.Component;

@Component
public class PratoPersistenceMapper {

    public Prato toDomain(PratoEntity entity) {
        return Prato.builder()
                .id(new Id(entity.getId()))
                .unidadeId(new Id(entity.getUnidadeId()))
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .preco(new Dinheiro(entity.getPrecoCentavos()))
                .disponivel(entity.isDisponivel())
                .ativo(entity.isAtivo())
                .build();
    }
}
