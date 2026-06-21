package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.RefreshToken;
import com.raizesdonordeste.app.infra.persistence.entity.RefreshTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenPersistenceMapper {

    public RefreshTokenEntity toEntity(RefreshToken refreshToken) {
        return new RefreshTokenEntity(
                refreshToken.getId().id(),
                refreshToken.getContaId().id(),
                refreshToken.getTokenHash(),
                refreshToken.getExpiraEm(),
                refreshToken.getRevogadoEm()
        );
    }

    public RefreshToken toDomain(RefreshTokenEntity entity) {
        return RefreshToken.builder()
                .id(new Id(entity.getId()))
                .contaId(new Id(entity.getContaId()))
                .tokenHash(entity.getTokenHash())
                .expiraEm(entity.getExpiraEm())
                .revogadoEm(entity.getRevogadoEm())
                .build();
    }
}
