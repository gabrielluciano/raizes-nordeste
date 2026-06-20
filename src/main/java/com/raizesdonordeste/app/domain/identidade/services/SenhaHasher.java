package com.raizesdonordeste.app.domain.identidade.services;

public interface SenhaHasher {

    String gerarHash(String senha);

    boolean verificarSenha(String senha, String senhaHash);
}
