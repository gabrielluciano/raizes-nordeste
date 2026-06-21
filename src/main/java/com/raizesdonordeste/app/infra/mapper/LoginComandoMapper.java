package com.raizesdonordeste.app.infra.mapper;

import com.raizesdonordeste.app.api.dto.LoginRequest;
import com.raizesdonordeste.app.domain.identidade.model.LoginComando;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoginComandoMapper {

    LoginComandoMapper INSTANCE = Mappers.getMapper(LoginComandoMapper.class);

    LoginComando toComando(LoginRequest login);
}
