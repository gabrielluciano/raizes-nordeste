package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.comum.model.CPF;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.Cliente;
import com.raizesdonordeste.app.domain.identidade.repository.ClienteRepository;
import com.raizesdonordeste.app.infra.persistence.jpa.ClienteJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.ClientePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClienteRepositoryImpl implements ClienteRepository {

    private final ClienteJpaRepository clienteJpaRepository;
    private final ClientePersistenceMapper mapper;

    @Override
    public Optional<Cliente> obterPorId(Id id) {
        return clienteJpaRepository.findById(id.id()).map(mapper::toDomain);
    }

    @Override
    public Optional<Cliente> obterPorContaId(Id contaId) {
        return clienteJpaRepository.findByContaId(contaId.id()).map(mapper::toDomain);
    }

    @Override
    public Optional<Cliente> obterPorCpf(CPF cpf) {
        return clienteJpaRepository.findByCpf(cpf.valor()).map(mapper::toDomain);
    }

    @Override
    public void inserir(Cliente cliente) {
        clienteJpaRepository.save(mapper.toEntity(cliente));
    }

    @Override
    public void atualizar(Cliente cliente) {
        clienteJpaRepository.save(mapper.toEntity(cliente));
    }
}
