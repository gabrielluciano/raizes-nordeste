package com.raizesdonordeste.app.domain.cardapio.model;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Promocao {

    public static final double DESCONTO_MAXIMO = 50.0;

    private final Id id;
    private final Id pratoId;
    private final String descricao;
    private final double percentualDesconto;
    private final LocalDateTime dataHoraInicio;
    private final LocalDateTime dataHoraFim;
    private boolean ativa;

    @Builder
    public Promocao(Id id, Id pratoId, String descricao, double percentualDesconto, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, boolean ativa) {
        validarPercentualDesconto(percentualDesconto);
        validarDataHoraInicioEFim(
                Guarda.naoNulo(dataHoraInicio, "dataHoraInicio"),
                Guarda.naoNulo(dataHoraFim, "dataHoraFim"));

        this.id = Guarda.naoNulo(id, "id");
        this.pratoId = Guarda.naoNulo(pratoId, "pratoId");
        this.descricao = Guarda.naoVazio(descricao, "descricao");
        this.percentualDesconto = percentualDesconto;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.ativa = ativa;
    }

    private void validarPercentualDesconto(double percentualDesconto) {
        if (percentualDesconto <= 0 || percentualDesconto > DESCONTO_MAXIMO) {
            throw new IllegalArgumentException("percentual desconto deve estar entre 0 e " + DESCONTO_MAXIMO);
        }
    }

    private void validarDataHoraInicioEFim(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        if (!dataHoraFim.isAfter(dataHoraInicio)) {
            throw new IllegalStateException("dataHoraInicio deve ser antes de dataHoraFim");
        }
    }

    public boolean estaVigente(LocalDateTime agora) {
        return agora.isAfter(dataHoraInicio) && agora.isBefore(dataHoraFim);
    }

    public Dinheiro calcularDesconto(Dinheiro valor) {
        if (valor.centavos() <= 0) {
            throw new IllegalArgumentException("valor deve ser positivo");
        }

        return valor.porcentagem(percentualDesconto);
    }

    public void desativar() {
        this.ativa = false;
    }

    public boolean estaAtiva() {
        return this.ativa;
    }

    public boolean aplicaAoPrato(Id pratoId) {
        return this.pratoId.equals(pratoId);
    }
}
