package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.comum.model.Email;
import com.raizesdonordeste.app.domain.identidade.model.Conta;
import com.raizesdonordeste.app.domain.identidade.repository.ContaRepository;
import com.raizesdonordeste.app.infra.persistence.jpa.ContaJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.ContaPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContaRepositoryImpl implements ContaRepository {

    private final ContaJpaRepository contaJpaRepository;
    private final ContaPersistenceMapper mapper;

    @Override
    public Optional<Conta> obterPorEmail(Email email) {
        return contaJpaRepository.findByEmail(email.valor()).map(mapper::toDomain);
    }

    @Override
    public void inserir(Conta conta) {
        contaJpaRepository.save(mapper.toEntity(conta));
    }
}
