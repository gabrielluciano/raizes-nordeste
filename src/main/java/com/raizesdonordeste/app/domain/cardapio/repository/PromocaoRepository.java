package com.raizesdonordeste.app.domain.cardapio.repository;

import com.raizesdonordeste.app.domain.cardapio.model.Promocao;
import com.raizesdonordeste.app.domain.comum.model.Id;

import java.util.Set;

public interface PromocaoRepository {

    Set<Promocao> obterPromocoesAtivasParaPratos(Set<Id> pratoIds);
}
