package com.raizesdonordeste.app.domain.identidade.services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SenhaValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "senha123!",
            "SENHA123!",
            "SenhaABC!",
            "Senha123",
            "Sen1!",
            "Senha 123!"
    })
    void deveRetornarFalse_QuandoSenhaFraca(String senha) {
        assertFalse(SenhaValidator.validaSenha(senha));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Senha123!",
            "Minha@Senha1",
            "Abcdef1#",
            "XyZ789$%"
    })
    void deveRetornarTrue_QuandoSenhaForte(String senha) {
        assertTrue(SenhaValidator.validaSenha(senha));
    }
}
