package com.raizesdonordeste.app.domain.comum.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComEmailInvalido() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email(""))
                .withMessage("email inválido.");
    }

    @Test
    void deveSerConstruidoCorretamente_QuandoConstruidoComEmailValido() {
        assertDoesNotThrow(() -> new Email("email@examplo.com"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "usuario",
            "@email.com",
            "usuario@",
            "usuario@email",
            "usuario@.com",
            "usuario.com",
            "usuario @email.com",
            "usuario@email .com",
            "usuario@@email.com",
            "usuario@email..com",
            "usuário@email.com",
            "usuario@dominio",
            "usuario#email.com"
    })
    void deveRetornarFalse_QuandoEmailInvalido(String email) {
        assertFalse(Email.valido(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "usuario@email.com",
            "john.doe@gmail.com",
            "teste123@yahoo.com",
            "usuario+tag@email.com",
            "nome.sobrenome@empresa.com.br",
            "a@b.co",
            "abc_123@email.net",
            "user-01@dominio.org"
    })
    void deveRetornarTrue_QuandoEmailValido(String email) {
        assertTrue(Email.valido(email));
    }
}
