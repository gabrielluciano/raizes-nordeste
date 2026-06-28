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
                funcionario.id().id(),
                funcionario.contaId().id(),
                funcionario.unidadeId().id(),
                funcionario.nome(),
                funcionario.telefone().valor(),
                funcionario.endereco(),
                funcionario.dataNascimento()
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
