package com.raizesdonordeste.app.domain.fidelidade.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RegrasFidelidadeTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarRegras()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComValorPorPontoNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarRegras()
                                .valorPorPonto(null)
                                .build())
                .withMessage("valorPorPonto não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComAcumuloPorCentavoNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarRegras()
                                .acumuloPorCentavo(null)
                                .build())
                .withMessage("acumuloPorCentavo não pode ser nulo.");
    }

    private RegrasFidelidade.RegrasFidelidadeBuilder criarRegras() {
        return RegrasFidelidade.builder()
                .id(Id.aleatorio())
                .valorPorPonto(BigDecimal.TEN)
                .acumuloPorCentavo(BigDecimal.valueOf(0.01))
                .validadePontosMeses(6)
                .tetoResgatePercentual(20);
    }
}
