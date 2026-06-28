package com.raizesdonordeste.app.infra.mapper;

import com.raizesdonordeste.app.api.dto.ExtratoFidelidadeResponse;
import com.raizesdonordeste.app.domain.fidelidade.model.ExtratoFidelidade;
import com.raizesdonordeste.app.domain.fidelidade.model.MovimentacaoPontos;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExtratoFidelidadeResponseMapper {

    public ExtratoFidelidadeResponse toResponse(ExtratoFidelidade extrato) {
        List<ExtratoFidelidadeResponse.MovimentacaoResponse> movimentacoes = extrato.movimentacoes().stream()
                .map(this::toMovimentacaoResponse)
                .toList();

        return new ExtratoFidelidadeResponse(extrato.saldoPontos(), movimentacoes);
    }

    private ExtratoFidelidadeResponse.MovimentacaoResponse toMovimentacaoResponse(MovimentacaoPontos movimentacao) {
        return new ExtratoFidelidadeResponse.MovimentacaoResponse(
                movimentacao.id().toString(),
                movimentacao.pedidoId() == null ? null : movimentacao.pedidoId().toString(),
                movimentacao.tipo(),
                movimentacao.pontos(),
                movimentacao.dataContabilizacao(),
                movimentacao.dataExpiracao()
        );
    }
}
