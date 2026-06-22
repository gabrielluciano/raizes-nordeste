package com.raizesdonordeste.app.infra.mapper;

import com.raizesdonordeste.app.api.dto.CadastroFuncionarioRequest;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.identidade.model.CadastrarFuncionarioComando;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CadastrarFuncionarioComandoMapper {

    CadastrarFuncionarioComandoMapper INSTANCE = Mappers.getMapper(CadastrarFuncionarioComandoMapper.class);

    CadastrarFuncionarioComando toComando(CadastroFuncionarioRequest cadastro);

    default Id uuidParaId(UUID uuid) {
        return uuid == null ? null : new Id(uuid);
    }
}
