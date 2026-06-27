package com.raizesdonordeste.app.infra.mapper;

import com.raizesdonordeste.app.api.dto.ConfirmacaoPixRequest;
import com.raizesdonordeste.app.domain.pagamento.model.ConfirmarPagamentoPixComando;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ConfirmarPagamentoPixComandoMapper {

    ConfirmarPagamentoPixComandoMapper INSTANCE = Mappers.getMapper(ConfirmarPagamentoPixComandoMapper.class);

    ConfirmarPagamentoPixComando toComando(ConfirmacaoPixRequest request);
}
