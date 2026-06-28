package com.raizesdonordeste.app.domain.fidelidade.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;

import java.time.LocalDateTime;

public record MovimentacaoPontos(
        Id id,
        Id clienteId,
        Id pedidoId,
        TipoMovPontos tipo,
        long pontos,
        LocalDateTime dataContabilizacao,
        LocalDateTime dataExpiracao) {

    @Builder
    public MovimentacaoPontos(
            Id id,
            Id clienteId,
            Id pedidoId,
            TipoMovPontos tipo,
            long pontos,
            LocalDateTime dataContabilizacao,
            LocalDateTime dataExpiracao) {
        this.id = Guarda.naoNulo(id, "id");
        this.clienteId = Guarda.naoNulo(clienteId, "clienteId");
        this.pedidoId = pedidoId;
        this.tipo = Guarda.naoNulo(tipo, "tipo");
        this.pontos = Guarda.positivo(pontos, "pontos");
        this.dataContabilizacao = Guarda.naoNulo(dataContabilizacao, "dataContabilizacao");
        this.dataExpiracao = dataExpiracao;
        validaDataExpiracao();
    }

    private void validaDataExpiracao() {
        boolean acumulo = TipoMovPontos.ACUMULO.equals(tipo);

        if (!acumulo && dataExpiracao != null) {
            throw new IllegalStateException("dataExpiracao só pode ser informada para tipo ACUMULO.");
        }

        if (acumulo && dataExpiracao == null) {
            throw new IllegalStateException("dataExpiracao deve ser informada para tipo ACUMULO.");
        }

        if (acumulo && !dataExpiracao.isAfter(dataContabilizacao)) {
            throw new IllegalStateException("dataExpiracao deve ser após dataContabilizacao.");
        }
    }

    public static MovimentacaoPontos acumulo(long pontos, Id clienteId, Id pedidoId, LocalDateTime dataContabilizacao, LocalDateTime dataExpiracao) {
        return new MovimentacaoPontos(Id.aleatorio(), clienteId, pedidoId, TipoMovPontos.ACUMULO, pontos, dataContabilizacao, dataExpiracao);
    }

    public static MovimentacaoPontos resgate(long pontos, Id clienteId, Id pedidoId, LocalDateTime dataContabilizacao) {
        return new MovimentacaoPontos(Id.aleatorio(), clienteId, pedidoId, TipoMovPontos.RESGATE, pontos, dataContabilizacao, null);
    }
}
