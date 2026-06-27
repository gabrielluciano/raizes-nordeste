package com.raizesdonordeste.app.domain.pagamento.repository;

import com.raizesdonordeste.app.domain.pagamento.model.Pagamento;

import java.util.Optional;

public interface PagamentoRepository {

    void inserir(Pagamento pagamento);

    void atualizar(Pagamento pagamento);

    Optional<Pagamento> obterPorTransacaoGateway(String idTransacaoGateway);

    Optional<Pagamento> obterPorIdempotencyKey(String idempotencyKey);
}
