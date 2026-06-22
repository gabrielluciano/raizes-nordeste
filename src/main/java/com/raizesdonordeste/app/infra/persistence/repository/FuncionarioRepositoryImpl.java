package com.raizesdonordeste.app.infra.persistence.repository;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.Funcionario;
import com.raizesdonordeste.app.domain.identidade.repository.FuncionarioRepository;
import com.raizesdonordeste.app.infra.persistence.jpa.FuncionarioJpaRepository;
import com.raizesdonordeste.app.infra.persistence.mapper.FuncionarioPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FuncionarioRepositoryImpl implements FuncionarioRepository {

    private final FuncionarioJpaRepository funcionarioJpaRepository;
    private final FuncionarioPersistenceMapper mapper;

    @Override
    public void inserir(Funcionario funcionario) {
        funcionarioJpaRepository.save(mapper.toEntity(funcionario));
    }

    @Override
    public Optional<Funcionario> obterPorContaId(Id contaId) {
        return funcionarioJpaRepository.findByContaId(contaId.id()).map(mapper::toDomain);
    }
}
