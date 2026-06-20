package com.raizesdonordeste.app.domain.identidade.model;

import com.raizesdonordeste.app.domain.comum.model.Id;
import com.raizesdonordeste.app.domain.comum.model.Slug;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BaseTest {

    @Test
    void deveLancarExcecao_QuandoConstruidoComIdNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarBase()
                                .id(null)
                                .build())
                .withMessage("id não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComSlugNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarBase()
                                .slug(null)
                                .build())
                .withMessage("slug não pode ser nulo.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComNomeBlank() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarBase()
                                .nome(null)
                                .build())
                .withMessage("nome não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarBase()
                                .nome("")
                                .build())
                .withMessage("nome não pode ser nulo ou vazio.");
    }

    @Test
    void deveLancarExcecao_QuandoConstruidoComRoleNullOuVazio() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarBase()
                                .rolesPermitidas(null)
                                .build())
                .withMessage("rolesPermitidas não pode ser nulo ou vazio.");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                        criarBase()
                                .rolesPermitidas(Set.of())
                                .build())
                .withMessage("rolesPermitidas não pode ser nulo ou vazio.");
    }

    @Test
    void deveRetornarTrue_QuandoRoleEhPermitida() {
        Base base = criarBase().build();

        boolean ehPermitida = base.permiteRole(Role.CLIENTE);

        assertThat(ehPermitida).isTrue();
    }

    @Test
    void deveRetornarFalse_QuandoRoleNaoEhPermitida() {
        Base base = criarBase().build();

        boolean ehPermitida = base.permiteRole(Role.ADMINISTRADOR);

        assertThat(ehPermitida).isFalse();
    }

    @Test
    void deveRetornarFalse_QuandoRoleNaoEhNull() {
        Base base = criarBase().build();

        boolean ehPermitida = base.permiteRole(null);

        assertThat(ehPermitida).isFalse();
    }

    private Base.BaseBuilder criarBase() {
        return Base.builder()
                .id(Id.aleatorio())
                .nome("Clientes")
                .slug(new Slug("clientes"))
                .rolesPermitidas(Set.of(Role.CLIENTE));
    }
}
