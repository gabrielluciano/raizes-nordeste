package com.raizesdonordeste.app.domain.identidade.repository;

import com.raizesdonordeste.app.domain.identidade.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    void inserir(RefreshToken refreshToken);

    Optional<RefreshToken> obterPorHash(String hash);
}
