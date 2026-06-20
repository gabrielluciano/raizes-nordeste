package com.raizesdonordeste.app.domain.identidade.repository;

import com.raizesdonordeste.app.domain.comum.model.CPF;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;

import java.util.Optional;

public interface ClienteRepository {

    Optional<Cliente> obterPorId(Id id);
    Optional<Cliente> obterPorCpf(CPF cpf);
    void atualizar(Cliente cliente);
}
