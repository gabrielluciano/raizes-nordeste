package com.raizesdonordeste.app.api.dto;

import com.raizesdonordeste.app.domain.fidelidade.model.TipoMovPontos;

import java.time.LocalDateTime;
import java.util.List;

public record ExtratoFidelidadeResponse(
        long saldoPontos,
        List<MovimentacaoResponse> movimentacoes
) {

    public record MovimentacaoResponse(
            String id,
            String pedidoId,
            TipoMovPontos tipo,
            long pontos,
            LocalDateTime dataContabilizacao,
            LocalDateTime dataExpiracao
    ) {
    }
}
