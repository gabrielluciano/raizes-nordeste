package com.raizesdonordeste.app.domain.comum.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DinheiroTest {

    @Test
    void deveSomarValor() {
        Dinheiro dinheiro = new Dinheiro(1000L);

        Dinheiro resultado = dinheiro.somar(dinheiro);

        assertThat(resultado.centavos()).isEqualTo(2000L);
    }

    @Test
    void deveSubtrairValor() {
        Dinheiro dinheiro = new Dinheiro(1000L);

        Dinheiro resultado = dinheiro.subtrair(dinheiro);

        assertThat(resultado.centavos()).isEqualTo(0L);
    }

    @Test
    void deveLancarExcecao_QuandoOverflowAoSomarValor() {
        Dinheiro dinheiro = new Dinheiro(Long.MAX_VALUE);

        assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> dinheiro.somar(new Dinheiro(10L)));
    }

    @Test
    void deveLancarExcecao_QuandoOverflowAoSubtrairValor() {
        Dinheiro dinheiro = new Dinheiro(Long.MIN_VALUE);

        assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> dinheiro.subtrair(new Dinheiro(10L)));
    }

    @Test
    void deveCalcularPorcentagem() {
        Dinheiro dinheiro = new Dinheiro(1000L);

        assertThat(dinheiro.porcentagem(10).centavos()).isEqualTo(100L);
    }

    @Test
    void deveArredondarCorretamente_AoCalcularPorcentagem() {
        Dinheiro dinheiro = new Dinheiro(1000L);

        assertThat(dinheiro.porcentagem(67.77).centavos()).isEqualTo(678);
        assertThat(dinheiro.porcentagem(67.22).centavos()).isEqualTo(672);
    }

    @Test
    void deveMultiplicarCorretamente() {
        Dinheiro dinheiro = new Dinheiro(1000L);

        assertThat(dinheiro.multiplicar(100).centavos()).isEqualTo(100000L);
    }

    @Test
    void deveLancarExcecao_QuandoOverflowAoMultiplicar() {
        Dinheiro dinheiro = new Dinheiro(Long.MAX_VALUE);

        assertThatExceptionOfType(ArithmeticException.class)
                .isThrownBy(() -> dinheiro.multiplicar(2));
    }

    @Test
    void deveDividirCorretamente() {
        assertThat(new Dinheiro(1000L).dividir(2)).isEqualTo(new Dinheiro(500L));
        assertThat(new Dinheiro(10L).dividir(3)).isEqualTo(new Dinheiro(3L));
        assertThat(new Dinheiro(20).dividir(3)).isEqualTo(new Dinheiro(6L));
    }
}
