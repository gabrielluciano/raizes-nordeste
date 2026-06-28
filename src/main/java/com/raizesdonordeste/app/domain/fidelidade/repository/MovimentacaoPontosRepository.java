package com.raizesdonordeste.app.domain.fidelidade.repository;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.fidelidade.model.MovimentacaoPontos;

import java.util.List;

public interface MovimentacaoPontosRepository {

    void inserir(MovimentacaoPontos movimentacaoPontos);

    List<MovimentacaoPontos> obterMovimentacoesCliente(Id clienteId);
}
