package com.raizesdonordeste.app.infra.mapper;

import com.raizesdonordeste.app.api.dto.CadastroClienteRequest;
import com.raizesdonordeste.app.domain.identidade.model.CadastrarClienteComando;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CadastrarClienteComandoMapper {

    CadastrarClienteComandoMapper INSTANCE = Mappers.getMapper(CadastrarClienteComandoMapper.class);

    CadastrarClienteComando toComando(CadastroClienteRequest cadastro);
}
