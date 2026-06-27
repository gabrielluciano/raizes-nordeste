package com.raizesdonordeste.app.infra.persistence.mapper;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.pagamento.model.FormaPagamento;
import com.raizesdonordeste.app.domain.pagamento.model.Pagamento;
import com.raizesdonordeste.app.domain.pagamento.model.StatusPagamento;
import com.raizesdonordeste.app.infra.persistence.entity.PagamentoEntity;
import org.springframework.stereotype.Component;

@Component
public class PagamentoPersistenceMapper {

    public PagamentoEntity toEntity(Pagamento domain) {
        return new PagamentoEntity(
                domain.getId().id(),
                domain.getPedidoId().id(),
                domain.getIdempotencyKey(),
                domain.getForma().name(),
                domain.getStatus().name(),
                domain.getValor().centavos(),
                domain.getIdTransacaoGateway(),
                domain.getDataSolicitacao(),
                domain.getDataConfirmacao(),
                domain.getMotivoRecusa(),
                domain.getQrCode(),
                domain.getQrCodeValidoAte()
        );
    }

    public Pagamento toDomain(PagamentoEntity entity) {
        return Pagamento.builder()
                .id(new Id(entity.getId()))
                .pedidoId(new Id(entity.getPedidoId()))
                .idempotencyKey(entity.getIdempotencyKey())
                .forma(FormaPagamento.valueOf(entity.getForma()))
                .status(StatusPagamento.valueOf(entity.getStatus()))
                .valor(new Dinheiro(entity.getValorCentavos()))
                .idTransacaoGateway(entity.getIdTransacaoGateway())
                .dataSolicitacao(entity.getDataSolicitacao())
                .dataConfirmacao(entity.getDataConfirmacao())
                .motivoRecusa(entity.getMotivoRecusa())
                .qrCode(entity.getQrCode())
                .qrCodeValidoAte(entity.getQrCodeValidoAte())
                .build();
    }
}
