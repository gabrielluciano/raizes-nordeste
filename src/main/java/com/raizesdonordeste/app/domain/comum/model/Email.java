package com.raizesdonordeste.app.domain.comum.model;

import java.util.regex.Pattern;

public record Email(String valor) {

    public static final Pattern REGEX = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)+$");

    public Email {
        if (!valido(valor)) {
            throw new IllegalArgumentException("email inválido.");
        }
    }

    public static boolean valido(String email) {
        if (email == null) {
            return false;
        }

        return REGEX.matcher(email).matches();
    }
}
