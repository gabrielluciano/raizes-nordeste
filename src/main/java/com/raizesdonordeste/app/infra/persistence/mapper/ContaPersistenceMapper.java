package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.comum.model.Email;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.Conta;
import com.raizesdonordeste.app.infra.persistence.entity.ContaEntity;
import org.springframework.stereotype.Component;

@Component
public class ContaPersistenceMapper {

    public ContaEntity toEntity(Conta conta) {
        return new ContaEntity(
                conta.getId().id(),
                conta.getEmail().valor(),
                conta.getSenhaHash(),
                conta.getStatus(),
                conta.getRole()
        );
    }

    public Conta toDomain(ContaEntity entity) {
        return Conta.builder()
                .id(new Id(entity.getId()))
                .email(new Email(entity.getEmail()))
                .senhaHash(entity.getSenhaHash())
                .status(entity.getStatus())
                .role(entity.getRole())
                .build();
    }
}
