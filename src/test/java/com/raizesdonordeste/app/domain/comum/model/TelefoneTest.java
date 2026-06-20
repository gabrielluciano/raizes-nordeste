package com.raizesdonordeste.app.domain.comum.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class TelefoneTest {


    @Test
    void deveLancarExcecao_QuandoConstruidoComTelefoneInvalido() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Telefone("abcdefghijk"))
                .withMessage("telefone inválido.");
    }

    @Test
    void deveSerConstruidoCorretamente_QuandoConstruidoComTelefoneValido() {
        assertDoesNotThrow(() -> new Telefone("21999998888"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "123",
            "01987654321",
            "00987654321",
            "119876543",
            "119876543210",
            "(11)987654321",
            "11-987654321",
            "11 987654321",
            "abcdefghijk"
    })
    void deveRetornarFalse_QuandoTelefoneInvalido(String telefone) {
        assertFalse(Telefone.valido(telefone));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1133334444",
            "2133334444",
            "3134567890",
            "11987654321",
            "21999998888",
            "31912345678"
    })
    void deveRetornarTrue_QuandoTelefoneValido(String telefone) {
        assertTrue(Telefone.valido(telefone));
    }
}
