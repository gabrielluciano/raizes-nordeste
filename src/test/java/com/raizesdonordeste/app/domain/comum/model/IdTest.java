package com.raizesdonordeste.app.domain.comum.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class IdTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComUUIDNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Id(null))
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveSerConstruidoCorretamente_QuandoConstruidoComUUIDValido() {
        assertDoesNotThrow(() -> new Id(UUID.randomUUID()));
    }
}
