package com.raizesdonordeste.app.domain.identidade.repository;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.Funcionario;

import java.util.Optional;

public interface FuncionarioRepository {

    Optional<Funcionario> obterPorContaId(Id contaId);

    void inserir(Funcionario funcionario);
}
