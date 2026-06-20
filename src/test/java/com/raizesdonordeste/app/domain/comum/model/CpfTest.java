package com.raizesdonordeste.app.domain.comum.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class CpfTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComCpfInvalido() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new CPF(""))
                .withMessage("cpf inválido.");
    }

    @Test
    void deveSerConstruidoCorretamente_QuandoConstruidoComCpfValido() {
        assertDoesNotThrow(() -> new CPF("52998224725"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "123",
            "abc",
            "1234567890",
            "123456789012",
            "123.456.789",
            "123-456-789-00",
            "123.456.789-0",
            "123.456.789-000",
            "52998224724", // DV alterado
            "11144477734", // DV alterado
            "12345678900", // DV incorreto
            "11111111111", // sequência repetida
            "00000000000", // sequência repetida
            "22222222222", // sequência repetida
            "93541134781", // DV alterado
            "28625587888"  // DV alterado
    })
    void deveRetornarFalse_QuandoCpfInvalido(String cpf) {
        assertFalse(CPF.valido(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "52998224725",
            "11144477735",
            "12345678909",
            "93541134780",
            "28625587887"
    })
    void deveRetornarTrue_QuandoCpfValido(String cpf) {
        assertTrue(CPF.valido(cpf));
    }
}
