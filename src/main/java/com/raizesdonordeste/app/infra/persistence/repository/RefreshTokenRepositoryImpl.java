package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.identidade.model.RefreshToken;
import com.raizesdonordeste.app.domain.identidade.repository.RefreshTokenRepository;
import com.raizesdonordeste.app.infra.persistence.jpa.RefreshTokenJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.RefreshTokenPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final RefreshTokenPersistenceMapper mapper;

    @Override
    public void inserir(RefreshToken refreshToken) {
        refreshTokenJpaRepository.save(mapper.toEntity(refreshToken));
    }

    @Override
    public Optional<RefreshToken> obterPorHash(String hash) {
        return refreshTokenJpaRepository.findByTokenHash(hash).map(mapper::toDomain);
    }
}
