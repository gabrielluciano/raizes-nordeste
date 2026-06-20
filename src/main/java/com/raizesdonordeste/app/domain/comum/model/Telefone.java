package com.raizesdonordeste.app.domain.comum.model;

import java.util.regex.Pattern;

public record Telefone(String valor) {

    public static final Pattern REGEX = Pattern.compile("^[1-9]{2}(?:9\\d{8}|\\d{8})$");

    public Telefone {
        if (!valido(valor)) {
            throw new IllegalArgumentException("telefone inválido.");
        }
    }

    public static boolean valido(String telefone) {
        if (telefone == null) {
            return false;
        }

        return REGEX.matcher(telefone).matches();
    }
}
