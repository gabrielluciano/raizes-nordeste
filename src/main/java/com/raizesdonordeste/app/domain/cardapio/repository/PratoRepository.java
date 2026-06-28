package com.raizesdonordeste.app.domain.cardapio.repository;

import com.raizesdonordeste.app.domain.cardapio.model.Prato;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Pagina;
import com.raizesdonordeste.app.domain.comum.model.Paginacao;

import java.util.Set;

public interface PratoRepository {

    Set<Prato> obterPratosPorIds(Set<Id> ids);

    Pagina<Prato> obterPaginaDeAtivosEDisponiveisPorUnidadeId(Id unidadeId, Paginacao paginacao);
}
