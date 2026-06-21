package com.raizesdonordeste.app.domain.identidade.services;

import com.raizesdonordeste.app.domain.identidade.model.Conta;

public interface ProvedorToken {

    String gerarAccessToken(Conta conta, int duracaoEmMinutos);

    String gerarRefreshToken();

    String hashRefreshToken(String token);
}
