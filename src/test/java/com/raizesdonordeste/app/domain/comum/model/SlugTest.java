package com.raizesdonordeste.app.domain.comum.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class SlugTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComSlugInvalido() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Slug(""))
                .withMessage("slug inválido.");
    }

    @Test
    void deveSerConstruidoCorretamente_QuandoConstruidoComSlugValido() {
        assertDoesNotThrow(() -> new Slug("slug"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "-slug",
            "slug-",
            "slug--test",
            "Slug",
            "slug test",
            "slug_test",
            "slugão"
    })
    void deveRetornarFalse_QuandoSlugInvalido(String slug) {
        assertFalse(Slug.valido(slug));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "slug",
            "slug-test",
            "slug-123",
    })
    void deveRetornarTrue_QuandoSlugValido(String slug) {
        assertTrue(Slug.valido(slug));
    }
}
