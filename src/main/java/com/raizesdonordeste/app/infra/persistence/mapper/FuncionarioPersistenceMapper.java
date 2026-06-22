package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Telefone;
import com.raizesdonordeste.app.domain.identidade.model.Funcionario;
import com.raizesdonordeste.app.infra.persistence.entity.FuncionarioEntity;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioPersistenceMapper {

    public FuncionarioEntity toEntity(Funcionario funcionario) {
        return new FuncionarioEntity(
                funcionario.getId().id(),
                funcionario.getContaId().id(),
                funcionario.getUnidadeId().id(),
                funcionario.getNome(),
                funcionario.getTelefone().valor(),
                funcionario.getEndereco(),
                funcionario.getDataNascimento()
        );
    }

    public Funcionario toDomain(FuncionarioEntity entity) {
        return Funcionario.builder()
                .id(new Id(entity.getId()))
                .contaId(new Id(entity.getContaId()))
                .unidadeId(new Id(entity.getUnidadeId()))
                .nome(entity.getNome())
                .telefone(new Telefone(entity.getTelefone()))
                .endereco(entity.getEndereco())
                .dataNascimento(entity.getDataNascimento())
                .build();
    }
}
