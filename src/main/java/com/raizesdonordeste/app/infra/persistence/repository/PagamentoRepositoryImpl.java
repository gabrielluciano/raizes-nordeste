package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.pagamento.model.Pagamento;
import com.raizesdonordeste.app.domain.pagamento.repository.PagamentoRepository;
import com.raizesdonordeste.app.infra.persistence.jpa.PagamentoJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.PagamentoPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PagamentoRepositoryImpl implements PagamentoRepository {

    private final PagamentoJpaRepository pagamentoJpaRepository;
    private final PagamentoPersistenceMapper mapper;

    @Override
    public void inserir(Pagamento pagamento) {
        pagamentoJpaRepository.save(mapper.toEntity(pagamento));
    }

    @Override
    public void atualizar(Pagamento pagamento) {
        pagamentoJpaRepository.save(mapper.toEntity(pagamento));
    }

    @Override
    public Optional<Pagamento> obterPorTransacaoGateway(String idTransacaoGateway) {
        return pagamentoJpaRepository.findByIdTransacaoGateway(idTransacaoGateway)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Pagamento> obterPorIdempotencyKey(String idempotencyKey) {
        return pagamentoJpaRepository.findByIdempotencyKey(idempotencyKey)
                .map(mapper::toDomain);
    }
}
