package com.raizesdonordeste.app.domain.organizacao.repository;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.organizacao.model.Unidade;

import java.util.Optional;

public interface UnidadeRepository {

    Optional<Unidade> obterPorId(Id id);
}
