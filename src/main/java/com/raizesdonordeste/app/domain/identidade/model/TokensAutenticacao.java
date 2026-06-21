package com.raizesdonordeste.app.domain.identidade.model;

import java.time.Duration;

public record TokensAutenticacao(
        String accessToken,
        String refreshToken,
        Duration accessTokenExpiraEm,
        Duration refreshTokenExpiraEm
) {
}
