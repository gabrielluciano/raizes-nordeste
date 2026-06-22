package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.comum.model.Horario;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.organizacao.model.Unidade;
import com.raizesdonordeste.app.infra.persistence.entity.UnidadeEntity;
import org.springframework.stereotype.Component;

@Component
public class UnidadePersistenceMapper {

    public UnidadeEntity toEntity(Unidade unidade) {
        return new UnidadeEntity(
                unidade.getId().id(),
                unidade.getNome(),
                unidade.getEndereco(),
                unidade.getHorarioFuncionamento().horaDe(),
                unidade.getHorarioFuncionamento().horaAte(),
                unidade.isAtiva()
        );
    }

    public Unidade toDomain(UnidadeEntity entity) {
        return Unidade.builder()
                .id(new Id(entity.getId()))
                .nome(entity.getNome())
                .endereco(entity.getEndereco())
                .horarioFuncionamento(new Horario(entity.getHoraDe(), entity.getHoraAte()))
                .ativa(entity.isAtiva())
                .build();
    }
}
