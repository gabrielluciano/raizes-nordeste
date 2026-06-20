package com.raizesdonordeste.app.domain.fidelidade.model;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import lombok.Builder;

@Builder
public record RegrasFidelidade(
        Dinheiro valorPonto,
        double pontosGanhosCentavos,
        int validadePontosMeses,
        int tetoResgatePercentual
) {
}
