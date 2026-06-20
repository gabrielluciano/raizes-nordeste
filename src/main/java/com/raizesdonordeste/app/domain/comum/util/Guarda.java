package com.raizesdonordeste.app.domain.comum.util;

import com.raizesdonordeste.app.domain.comum.model.Dinheiro;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public final class Guarda {

    private Guarda() {
    }

    public static <T> T naoNulo(T valor, String campo) {
        if (valor == null) {
            throw new IllegalArgumentException("%s não pode ser nulo.".formatted(campo));
        }
        return valor;
    }

    public static String naoVazio(String valor, String campo) {
        if (StringUtils.isBlank(valor)) {
            throw new IllegalArgumentException("%s não pode ser nulo ou vazio.".formatted(campo));
        }
        return valor;
    }

    public static <T extends Collection<?>> T naoVazio(T valor, String campo) {
        if (valor == null || valor.isEmpty()) {
            throw new IllegalArgumentException("%s não pode ser nulo ou vazio.".formatted(campo));
        }
        return valor;
    }

    public static Dinheiro naoNegativo(Dinheiro valor, String campo) {
        naoNulo(valor, campo);

        if (valor.centavos() < 0) {
            throw new IllegalArgumentException("%s valor não pode ser negativo.".formatted(campo));
        }

        return valor;
    }

    public static Dinheiro positivo(Dinheiro valor, String campo) {
        naoNulo(valor, campo);

        if (valor.centavos() <= 0) {
            throw new IllegalArgumentException("%s valor deve ser positivo.".formatted(campo));
        }

        return valor;
    }

    public static <T extends Number> T positivo(T valor, String campo) {
        naoNulo(valor, campo);

        if (valor.doubleValue() <= 0) {
            throw new IllegalArgumentException("%s valor deve ser positivo.".formatted(campo));
        }

        return valor;
    }
}
