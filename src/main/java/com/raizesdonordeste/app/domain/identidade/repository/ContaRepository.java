package com.raizesdonordeste.app.domain.identidade.repository;

import com.raizesdonordeste.app.domain.comum.model.Email;
import com.raizesdonordeste.app.domain.identidade.model.Conta;

import java.util.Optional;

public interface ContaRepository {

    Optional<Conta> obterPorEmail(Email email);

    void inserir(Conta conta);
}
