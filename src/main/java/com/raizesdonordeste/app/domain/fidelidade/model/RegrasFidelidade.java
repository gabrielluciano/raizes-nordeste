package com.raizesdonordeste.app.domain.fidelidade.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.util.Guarda;
import lombok.Builder;

import java.math.BigDecimal;

public record RegrasFidelidade(Id id, BigDecimal valorPorPonto, BigDecimal acumuloPorCentavo, int validadePontosMeses,
                               int tetoResgatePercentual) {

    @Builder
    public RegrasFidelidade(Id id,
                            BigDecimal valorPorPonto,
                            BigDecimal acumuloPorCentavo,
                            int validadePontosMeses,
                            int tetoResgatePercentual) {
        this.id = Guarda.naoNulo(id, "id");
        this.valorPorPonto = Guarda.naoNulo(valorPorPonto, "valorPorPonto");
        this.acumuloPorCentavo = Guarda.naoNulo(acumuloPorCentavo, "acumuloPorCentavo");
        this.validadePontosMeses = validadePontosMeses;
        this.tetoResgatePercentual = tetoResgatePercentual;
    }
}
